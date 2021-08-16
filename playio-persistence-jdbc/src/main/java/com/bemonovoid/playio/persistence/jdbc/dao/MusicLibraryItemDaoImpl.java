package com.bemonovoid.playio.persistence.jdbc.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bemonovoid.playio.core.dao.MusicLibraryItemDao;
import com.bemonovoid.playio.core.exception.DatabaseItemNotFoundException;
import com.bemonovoid.playio.core.model.Album;
import com.bemonovoid.playio.core.model.Artist;
import com.bemonovoid.playio.core.model.ArtistAlbumSong;
import com.bemonovoid.playio.core.model.ArtistAlbumSongs;
import com.bemonovoid.playio.core.model.Song;
import com.bemonovoid.playio.core.service.LibraryItemFilter;
import com.bemonovoid.playio.core.service.PageableRequest;
import com.bemonovoid.playio.core.service.PageableResult;
import com.bemonovoid.playio.persistence.jdbc.entity.LibraryItemEntity;
import com.bemonovoid.playio.persistence.jdbc.repository.LibraryItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
    public PageableResult<Artist> getArtists(PageableRequest pageableRequest) {
        long total = getTotalArtistCount();

        if (total == 0) {
            return PageableResultImpl.empty();
        }

        PageRequest pageRequest = PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize());

        String pageableSql = "SELECT ARTIST_ID, ARTIST_NAME FROM " +
                LibraryItemEntity.TABLE_NAME + " GROUP BY ARTIST_ID, ARTIST_NAME LIMIT " + pageRequest.getPageSize() + " OFFSET " + pageRequest.getOffset();

        List<Artist> artists = jdbcTemplate.query(pageableSql, (rs, rowNum) -> Artist.builder()
                .id(rs.getString(1))
                .name(rs.getString(2))
                .build());
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
                        .totalSongs(entry.getValue().size())
                        .totalTimeInSeconds(albumTimeInSeconds)
                        .build());
            }
            return new PageableResultImpl<>(new PageImpl<>(albums));
        }
        return PageableResultImpl.empty();
    }

    @Override
    public ArtistAlbumSongs getArtistAlbumSongs(String albumId) {
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
                        .totalSongs(albumItems.size())
                        .totalTimeInSeconds(albumTimeInSeconds)
                        .build();
            }
            songs.add(songFromEntity(entity));
        }
        return new ArtistAlbumSongs(artist, album, songs);
    }

    @Override
    public ArtistAlbumSong getSong(long songId) {
        LibraryItemEntity entity = repository.findById(songId)
                .orElseThrow(() -> new DatabaseItemNotFoundException("Song", String.valueOf(songId)));
        Artist artist = artistFromEntity(entity);
        Album album = albumFromEntity(entity).build();
        Song song = songFromEntity(entity);
        return new ArtistAlbumSong(artist, album, song);
    }

    private long getTotalArtistCount() {
        String totalSql = "SELECT COUNT(DISTINCT ARTIST_ID) FROM " + LibraryItemEntity.TABLE_NAME;
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

    private Song songFromEntity(LibraryItemEntity entity) {
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
                .playCount(entity.getPlayCount())
                .build();
    }
}
