package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.dao.MediaLibraryDao;
import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.Album;
import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.Artist;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.ScannableItemInfo;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.service.AlbumSearchCriteria;
import com.bemonovoid.playsqd.core.service.ArtistSearchCriteria;
import com.bemonovoid.playsqd.core.service.PageableResult;
import com.bemonovoid.playsqd.persistence.jdbc.entity.LibraryItemEntity;
import com.bemonovoid.playsqd.persistence.jdbc.repository.LibraryItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
class MediaLibraryDaoImpl implements MediaLibraryDao {

    private final JdbcTemplate jdbcTemplate;
    private final LibraryItemRepository repository;

    MediaLibraryDaoImpl(JdbcTemplate jdbcTemplate, LibraryItemRepository repository) {
        this.jdbcTemplate = jdbcTemplate;
        this.repository = repository;
    }

    @Override
    public PageableResult<ArtistInfo> getArtists(ArtistSearchCriteria searchCriteria) {
        long total = getTotalArtistCount(searchCriteria);
        if (total == 0) {
            return PageableResultImpl.empty();
        }
        List<ArtistInfo> artistInfos;

        int page = searchCriteria.pageableInfo().page();
        int pageSize = searchCriteria.pageableInfo().pageSize();
        long offset = (page - 1L) * pageSize;

        if (StringUtils.hasText(searchCriteria.artistNameLike())) {
            artistInfos =
                    repository.pageableArtistsLike(pageSize, offset, searchCriteria.artistNameLike().toUpperCase());
        } else {
            artistInfos = repository.pageableArtists(pageSize, offset);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, searchCriteria.pageableInfo().pageSize());

        return new PageableResultImpl<>(new PageImpl<>(artistInfos, pageRequest, total));
    }

    @Override
    public PageableResult<AlbumInfo> getAlbums(AlbumSearchCriteria searchCriteria) {
        if (searchCriteria.artistId() != null) {
            List<AlbumInfo> artistAlbums = repository.artistAlbums(searchCriteria.artistId());
            return new PageableResultImpl<>(new PageImpl<>(artistAlbums));
        } else {
            long albumsCount;
            String albumNameLike = searchCriteria.albumName() == null ? "" : searchCriteria.albumName();
            if (albumNameLike.isBlank()) {
                albumsCount = repository.albumsCount();
            } else {
                albumsCount = repository.albumsLikeCount(albumNameLike.toUpperCase());
            }
            if (albumsCount == 0) {
                return PageableResultImpl.empty();
            }

            int page = searchCriteria.pageableInfo().page();
            int pageSize = searchCriteria.pageableInfo().pageSize();
            long offset = (page - 1L) * pageSize;

            List<AlbumInfo> albumInfos = repository.pageableAlbumsLike(pageSize, offset, albumNameLike.toUpperCase());

            PageRequest pageRequest = PageRequest.of(page - 1, searchCriteria.pageableInfo().pageSize());

            return new PageableResultImpl<>(new PageImpl<>(albumInfos, pageRequest, albumsCount));
        }
    }

    @Override
    public PageableResult<Song> getArtistAlbumSongs(String albumId) {
        Artist artist = null;
        Album album = null;

        List<LibraryItemEntity> albumItems = repository.findByAlbumId(albumId);

        if (albumItems.isEmpty()) {
            throw PlayqdException.objectDoesNotExistException("Album", albumId);
        }

        List<Song> songs = new ArrayList<>(albumItems.size());

        for (LibraryItemEntity entity : albumItems) {
            if (artist == null) {
                artist = artistFromEntity(entity);
            }
            if (album == null) {
                int albumTimeInSeconds =
                        albumItems.stream().mapToInt(LibraryItemEntity::getTrackLength).sum();
                album = albumFromEntity(entity)
                        .artist(artist)
                        .totalSongs(albumItems.size())
                        .totalTimeInSeconds(albumTimeInSeconds)
                        .build();
            }
            songs.add(songFromEntity(entity).artist(artist).album(album).build());
        }
        return new PageableResultImpl<>(new PageImpl<>(songs));
    }


    @Override
    public Song getSong(long songId) {
        LibraryItemEntity entity = getLibraryItemEntityById(songId);
        Artist artist = artistFromEntity(entity);
        Album album = albumFromEntity(entity).artist(artist).build();
        return songFromEntity(entity).artist(artist).album(album).build();
    }

    @Override
    public String getSongLocation(long songId) {
        return getLibraryItemEntityById(songId).getFileLocation();
    }

    @Override
    public String getFirstAlbumSongLocation(String albumId) {
        return repository.findAlbumSongLocation(albumId)
                .orElseThrow(() -> PlayqdException.objectDoesNotExistException("Album", String.valueOf(albumId)));
    }

    @Override
    public void updateFavoriteStatus(long songId) {
        Song song = getSong(songId);
        updateSong(songId, List.of(LibraryItemEntity.COL_MISC_IS_FAVORITE), !song.isFavorite());
    }

    @Override
    public Set<String> getAllLocations() {
        return Collections.synchronizedSet(new HashSet<>(jdbcTemplate.queryForList(
                String.format("SELECT %s FROM %s", LibraryItemEntity.COL_FILE_LOCATION, LibraryItemEntity.TABLE_NAME),
                String.class)));
    }

    @Override
    public void deleteAllByLocation(String location) {
        repository.deleteAllByFileLocationStartsWith(location);
    }

    @Override
    public int addLibraryItems(Stream<ScannableItemInfo> itemsStream) {
        int itemsScannedCount = 0;

        SqlParameterSource[] sqlParameterSources = itemsStream
                .map(DataMappers::audioFileToSqlParametersSource)
                .toArray(SqlParameterSource[]::new);

        if (sqlParameterSources.length == 0) {
            return itemsScannedCount;
        }

        SimpleJdbcInsert songsJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(LibraryItemEntity.TABLE_NAME);
        int[] rows = songsJdbcInsert.executeBatch(sqlParameterSources);
        return rows.length;
    }

    private void updateSong(long songId, List<String> columns, Object... args) {
        String columnsString = columns.stream().map(col -> col + " = ?").collect(Collectors.joining(", "));
        String sql = String.format("UPDATE %s SET %s WHERE %s = %s",
                LibraryItemEntity.TABLE_NAME, columnsString, LibraryItemEntity.COL_ID, songId);
        jdbcTemplate.update(sql, args);
    }

    private LibraryItemEntity getLibraryItemEntityById(long songId) {
        return repository.findById(songId)
                .orElseThrow(() -> PlayqdException.objectDoesNotExistException("Song", String.valueOf(songId)));
    }

    private long getTotalArtistCount(ArtistSearchCriteria searchCriteria) {
        if (StringUtils.hasText(searchCriteria.artistNameLike())) {
            return repository.artistsLikeCount(searchCriteria.artistNameLike());
        } else {
            return repository.artistsCount();
        }
    }

    private Artist artistFromEntity(LibraryItemEntity entity) {
        return Artist.builder().id(entity.getArtistId()).name(entity.getArtistName()).build();
    }

    private Album.AlbumBuilder albumFromEntity(LibraryItemEntity entity) {
        return Album.builder()
                .id(entity.getAlbumId())
                .name(entity.getAlbumName())
                .year(entity.getAlbumYear())
                .genre(entity.getAlbumGenre());
    }

    private Song.SongBuilder songFromEntity(LibraryItemEntity entity) {
        return Song.builder()
                .id(entity.getId())
                .name(entity.getTrackName())
                .comment(entity.getComment())
                .lyrics(entity.getLyrics())
                .trackId(entity.getTrackId())
                .trackLengthInSeconds(entity.getTrackLength())
                .audioBitRate(entity.getAudioBitRate())
                .audioChannelType(entity.getAudioChannelType())
                .audioEncodingType(entity.getAudioEncodingType())
                .audioSampleRate(entity.getAudioSampleRate())
                .fileName(entity.getFileName())
                .fileExtension(entity.getFileExtension())
                .fileLocation(entity.getFileLocation())
                .favorite(entity.isFavorite())
                .playCount(entity.getPlayCount());
    }

}
