package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.dao.MediaLibraryDao;
import com.bemonovoid.playsqd.core.exception.DatabaseItemNotFoundException;
import com.bemonovoid.playsqd.core.model.Album;
import com.bemonovoid.playsqd.core.model.Artist;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.service.LibraryItemFilter;
import com.bemonovoid.playsqd.core.service.PageableResult;
import com.bemonovoid.playsqd.core.service.PageableSearch;
import com.bemonovoid.playsqd.persistence.jdbc.entity.LibraryItemEntity;
import com.bemonovoid.playsqd.persistence.jdbc.repository.LibraryItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public PageableResult<ArtistInfo> getArtists(PageableSearch pageableSearch) {
        long total = getTotalArtistCount(pageableSearch);
        if (total == 0) {
            return PageableResultImpl.empty();
        }
        List<ArtistInfo> artistInfos;

        PageRequest pageRequest =
                PageRequest.of(pageableSearch.pageInfo().page() - 1, pageableSearch.pageInfo().pageSize());

        if (StringUtils.hasText(pageableSearch.search())) {
            artistInfos = repository.pageableArtistsLike(
                    pageRequest.getPageSize(), pageRequest.getOffset(), pageableSearch.search().toUpperCase());
        } else {
            artistInfos = repository.pageableArtists(pageRequest.getPageSize(), pageRequest.getOffset());
        }
        return new PageableResultImpl<>(new PageImpl<>(artistInfos, pageRequest, total));
    }

    @Override
    public PageableResult<Album> getAlbums(LibraryItemFilter libraryItemFilter) {
        if (libraryItemFilter.getId() != null) {
            String artistId = libraryItemFilter.getId();
            Map<String, List<LibraryItemEntity>> itemsByAlbumId = repository.findByArtistId(artistId)
                    .collect(Collectors.groupingBy(LibraryItemEntity::getAlbumId));

            if (itemsByAlbumId.isEmpty()) {
                throw new DatabaseItemNotFoundException("Artist", artistId);
            }

            Artist artist = null;

            List<Album> albums = new ArrayList<>(itemsByAlbumId.size());

            for (Map.Entry<String, List<LibraryItemEntity>> entry : itemsByAlbumId.entrySet()) {

                LibraryItemEntity itemEntity = entry.getValue().get(0);

                if (artist == null) {
                    artist = artistFromEntity(itemEntity);
                }

                int albumTimeInSeconds =
                        entry.getValue().stream().mapToInt(LibraryItemEntity::getSongTrackLength).sum();

                albums.add(albumFromEntity(itemEntity)
                        .artist(artist)
                        .totalSongs(entry.getValue().size())
                        .totalTimeInSeconds(albumTimeInSeconds)
                        .build());
            }
            return new PageableResultImpl<>(new PageImpl<>(albums));
        }
        return PageableResultImpl.empty();
    }

    @Override
    public PageableResult<Song> getArtistAlbumSongs(String albumId) {
        Artist artist = null;
        Album album = null;

        List<LibraryItemEntity> albumItems = repository.findByAlbumId(albumId);

        if (albumItems.isEmpty()) {
            throw new DatabaseItemNotFoundException("Album", albumId);
        }

        List<Song> songs = new ArrayList<>(albumItems.size());

        for (LibraryItemEntity entity : albumItems) {
            if (artist == null) {
                artist = artistFromEntity(entity);
            }
            if (album == null) {
                int albumTimeInSeconds =
                        albumItems.stream().mapToInt(LibraryItemEntity::getSongTrackLength).sum();
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
                .orElseThrow(() -> new DatabaseItemNotFoundException("Album", String.valueOf(albumId)));
    }

    @Override
    public void updateFavoriteStatus(long songId) {
        Song song = getSong(songId);
        updateSong(songId, List.of(LibraryItemEntity.COL_MISC_IS_FAVORITE), !song.isFavorite());
    }

    private void updateSong(long songId, List<String> columns, Object... args) {
        String columnsString = columns.stream().map(col -> col + " = ?").collect(Collectors.joining(", "));
        String sql = String.format("UPDATE %s SET %s WHERE %s = %s",
                LibraryItemEntity.TABLE_NAME, columnsString, LibraryItemEntity.COL_ID, songId);
        jdbcTemplate.update(sql, args);
    }

    private LibraryItemEntity getLibraryItemEntityById(long songId) {
        return repository.findById(songId)
                .orElseThrow(() -> new DatabaseItemNotFoundException("Song", String.valueOf(songId)));
    }

    private long getTotalArtistCount(PageableSearch request) {
        if (StringUtils.hasText(request.search())) {
            return repository.artistsLikeCount(request.search());
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
                .name(entity.getSongName())
                .comment(entity.getSongComment())
                .lyrics(entity.getSongLyrics())
                .trackId(entity.getSongTrackId())
                .trackLengthInSeconds(entity.getSongTrackLength())
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
