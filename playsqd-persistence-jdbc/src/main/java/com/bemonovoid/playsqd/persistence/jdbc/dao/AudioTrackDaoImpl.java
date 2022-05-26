package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.dao.AudioTrackDao;
import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.Album;
import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.Artist;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.model.Genre;
import com.bemonovoid.playsqd.core.model.ScannedAudioFileWithMetadata;
import com.bemonovoid.playsqd.core.model.channel.AudioTrackCountQueryResult;
import com.bemonovoid.playsqd.core.service.AlbumSearchCriteria;
import com.bemonovoid.playsqd.core.service.ArtistSearchCriteria;
import com.bemonovoid.playsqd.core.service.PageableResult;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioTrackEntity;
import com.bemonovoid.playsqd.persistence.jdbc.repository.AudioTrackRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
record AudioTrackDaoImpl(JdbcTemplate jdbcTemplate, AudioTrackRepository repository) implements AudioTrackDao {

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
    public List<AudioTrack> getAlbumTracks(String albumId) {
        return repository.findByAlbumIdOrderByTrackNumberAsc(albumId).stream()
                .map(this::fromEntity)
                .toList();
    }

    @Override
    public AudioTrackCountQueryResult getCountByGenres(Collection<String> genres) {
        return repository.countByGenreIn(genres);
    }

    @Override
    public AudioTrack getTrackById(long trackId) {
        return fromEntity(audioTrackEntityById(trackId));
    }

    @Override
    public Optional<AudioTrack> getRandomTrackByGenre(String genre) {
        return repository.findRandomTrackByGenre(genre)
                .map(this::fromEntity);
    }

    @Override
    public Optional<AudioTrack> findRandomGenreTrackNotYetPlayedByChannelId(long channelId, Collection<String> genres) {
        return repository.findRandomGenreTrackNotYetPlayedByChannelId(channelId, genres)
                .map(this::fromEntity);
    }

    @Override
    public boolean existsByAlbumGenreInIgnoreCase(Collection<String> genres) {
        return repository.existsByAlbumGenreInIgnoreCase(genres);
    }

    @Override
    public AudioTrack getFirstTrackByAlbumId(String albumId) {
        return repository.findFirstByAlbumId(albumId)
                .map(this::fromEntity)
                .orElseThrow(() -> PlayqdException.objectDoesNotExistException("Album", String.valueOf(albumId)));
    }

    @Override
    public void updateFavoriteStatus(long songId) {
        AudioTrack audioTrack = getTrackById(songId);
        updateAudioTrack(songId, List.of(AudioTrackEntity.COL_MISC_IS_FAVORITE), !audioTrack.favorite());
    }

    @Override
    public Set<String> getAllLocations() {
        return Collections.synchronizedSet(new HashSet<>(jdbcTemplate.queryForList(
                String.format("SELECT %s FROM %s", AudioTrackEntity.COL_FILE_LOCATION, AudioTrackEntity.TABLE_NAME),
                String.class)));
    }

    @Override
    public void deleteAllByLocation(String location) {
        repository.deleteAllByFileLocationStartsWith(location);
    }

    @Override
    public int mapToAudioTrackAndSave(Stream<ScannedAudioFileWithMetadata> scannedAudioFileWithMetadataStream) {
        int itemsScannedCount = 0;

        SqlParameterSource[] sqlParameterSources = scannedAudioFileWithMetadataStream
                .map(DataMappers::audioFileToSqlParametersSource)
                .toArray(SqlParameterSource[]::new);

        if (sqlParameterSources.length == 0) {
            return itemsScannedCount;
        }

        SimpleJdbcInsert songsJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(AudioTrackEntity.TABLE_NAME);
        int[] rows = songsJdbcInsert.executeBatch(sqlParameterSources);
        return rows.length;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return repository.findAllGenres();
    }

    @Override
    public void updateAlbumArtworkUrl(String albumId, String artworkUrl) {
        repository.setAlbumArtworkUrl(albumId, artworkUrl);
    }

    private void updateAudioTrack(long songId, List<String> columns, Object... args) {
        String columnsString = columns.stream().map(col -> col + " = ?").collect(Collectors.joining(", "));
        String sql = String.format("UPDATE %s SET %s WHERE %s = %s",
                AudioTrackEntity.TABLE_NAME, columnsString, AudioTrackEntity.COL_ID, songId);
        jdbcTemplate.update(sql, args);
    }

    private AudioTrackEntity audioTrackEntityById(long songId) {
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

    private AudioTrack fromEntity(AudioTrackEntity entity) {
        var artist = new Artist(entity.getArtistId(), entity.getArtistName());
        var album = new Album(
                entity.getAlbumId(),
                entity.getAlbumName(),
                entity.getAlbumYear(),
                entity.getAlbumGenre(),
                entity.getAlbumArtworkUrl());
        return new AudioTrack(
                entity.getId(),
                entity.getTrackName(),
                entity.getComment(),
                entity.getLyrics(),
                entity.isFavorite(),
                entity.getTrackNumber(),
                entity.getTrackLength(),
                entity.getAudioBitRate(),
                entity.getAudioChannelType(),
                entity.getAudioEncodingType(),
                entity.getAudioSampleRate(),
                entity.getFileExtension(),
                entity.getFileName(),
                entity.getPlayCount(),
                entity.getFileLocation(),
                artist,
                album);
    }

}
