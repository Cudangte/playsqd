package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.dao.MusicLibraryItemDao;
import com.bemonovoid.playsqd.core.exception.DatabaseItemNotFoundException;
import com.bemonovoid.playsqd.core.model.Album;
import com.bemonovoid.playsqd.core.model.Artist;
import com.bemonovoid.playsqd.core.model.ArtistListItem;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.service.LibraryItemFilter;
import com.bemonovoid.playsqd.core.service.PageableResult;
import com.bemonovoid.playsqd.core.service.PageableSearchRequest;
import com.bemonovoid.playsqd.persistence.jdbc.entity.LibraryItemEntity;
import com.bemonovoid.playsqd.persistence.jdbc.repository.LibraryItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
class MusicLibraryItemDaoImpl implements MusicLibraryItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final LibraryItemRepository repository;

    MusicLibraryItemDaoImpl(JdbcTemplate jdbcTemplate, LibraryItemRepository repository) {
        this.jdbcTemplate = jdbcTemplate;
        this.repository = repository;
    }

    @Override
    public PageableResult<ArtistListItem> getArtists(PageableSearchRequest pageableSearchRequest) {
        long total = getTotalArtistCount(pageableSearchRequest);

        if (total == 0) {
            return PageableResultImpl.empty();
        }

        PageRequest pageRequest = PageRequest.of(pageableSearchRequest.getPage(), pageableSearchRequest.getPageSize());

        String pageableSql = " GROUP BY " + LibraryItemEntity.COL_ARTIST_ID + "," + LibraryItemEntity.COL_ARTIST_NAME +
                " LIMIT " + pageRequest.getPageSize() + " OFFSET " + pageRequest.getOffset();

        String sql = "SELECT " + LibraryItemEntity.COL_ARTIST_ID + "," + LibraryItemEntity.COL_ARTIST_NAME +
                ",COUNT(DISTINCT " + LibraryItemEntity.COL_ALBUM_ID + "),COUNT(" + LibraryItemEntity.COL_ID + ")" +
                " FROM " + LibraryItemEntity.TABLE_NAME;

        if (StringUtils.hasText(pageableSearchRequest.getSearch())) {
            String likeInput = pageableSearchRequest.getSearch().toUpperCase();
            sql += " WHERE UPPER(" + LibraryItemEntity.COL_ARTIST_NAME + ") LIKE '%" + likeInput + "%'";
        }

        sql += pageableSql;

        List<ArtistListItem> artists = jdbcTemplate.query(sql,
                (rs, rowNum) -> new ArtistListItem(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
        return new PageableResultImpl<>(new PageImpl<>(artists, pageRequest, total));
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
    public Collection<Song> getArtistAlbumSongs(String albumId) {
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
        return songs;
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
        String sql = "SELECT " + LibraryItemEntity.COL_FILE_LOCATION + " FROM " + LibraryItemEntity.TABLE_NAME +
                " WHERE " + LibraryItemEntity.COL_ALBUM_ID + " = ? LIMIT 1" ;
        List<String> result = jdbcTemplate.queryForList(sql, String.class, albumId);
        if (result.isEmpty()) {
            throw new DatabaseItemNotFoundException("Album", String.valueOf(albumId));
        }
        return result.get(0);
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

    private long getTotalArtistCount(PageableSearchRequest request) {
        String totalSql = "SELECT COUNT(DISTINCT " + LibraryItemEntity.COL_ARTIST_ID + ") FROM " + LibraryItemEntity.TABLE_NAME;
        if (StringUtils.hasText(request.getSearch())) {
            String likeInput = request.getSearch().toUpperCase();
            totalSql += " WHERE UPPER(" + LibraryItemEntity.COL_ARTIST_NAME + ") LIKE '%" + likeInput + "%'";
        }
        return Optional.ofNullable(jdbcTemplate.queryForObject(totalSql, Long.class)).orElse(0L);
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
