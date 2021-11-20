package com.bemonovoid.playsqd.directory.scanner.impl;

import com.bemonovoid.playsqd.core.audio.AudioFile;
import com.bemonovoid.playsqd.core.audio.AudioFileReader;
import com.bemonovoid.playsqd.core.dao.MediaDirectoryScanLogDao;
import com.bemonovoid.playsqd.core.dao.MediaSourceDao;
import com.bemonovoid.playsqd.core.exception.MediaSourceException;
import com.bemonovoid.playsqd.core.model.DirectoryScanStatus;
import com.bemonovoid.playsqd.core.model.MediaSource;
import com.bemonovoid.playsqd.core.model.MusicDirectoryScanLog;
import com.bemonovoid.playsqd.core.service.MediaSourceScanner;
import com.bemonovoid.playsqd.core.service.ScannerOptions;
import com.bemonovoid.playsqd.persistence.jdbc.entity.LibraryItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Component
class FileDirectoryScanner implements MediaSourceScanner {

    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of("flac", "m4a", "m4p", "mp3", "ogg", "wav", "wma");

    private final Map<String, String> artistIds = Collections.synchronizedMap(new LinkedHashMap<>());
    private final Map<String, Map<String, String>> albumIds = Collections.synchronizedMap(new LinkedHashMap<>());

    private final JdbcTemplate jdbcTemplate;
    private final MediaDirectoryScanLogDao directoryScanLogDao;
    private final AudioFileReader audioFileReader;


    FileDirectoryScanner(JdbcTemplate jdbcTemplate,
                         MediaDirectoryScanLogDao directoryScanLogDao,
                         AudioFileReader audioFileReader) {
        this.jdbcTemplate = jdbcTemplate;
        this.directoryScanLogDao = directoryScanLogDao;
        this.audioFileReader = audioFileReader;
    }

    @Override
    @Async
    public void scan(Collection<MediaSource> mediaSources, ScannerOptions scannerOptions) {
        prepare(scannerOptions);
        for (MediaSource mediaSource : mediaSources) {

//            Path path = Path.of(mediaSource.path());

            MusicDirectoryScanLog.MusicDirectoryScanLogBuilder logBuilder = MusicDirectoryScanLog.builder()
                    .scanDirectory(mediaSource.path())
                    .scanStatus(DirectoryScanStatus.IN_PROGRESS)
                    .deleteMissing(scannerOptions.deleteMissing())
                    .deleteAllBeforeScan(scannerOptions.deleteAllBeforeScan());

            Set<String> indexedFiles = getAlreadyIndexedFiles();

            LocalTime scanStartedAt = LocalTime.now();

            SqlParameterSource[] sqlParameterSources = getSqlParametersSourceFromPath(mediaSource, indexedFiles);

            if (sqlParameterSources.length == 0) {
                log.debug("Scanned source: {}. No files from the source were indexed. " +
                        "Source might have bene empty or the source has been indexed already", mediaSource);
            }

            try {
                SimpleJdbcInsert songsJdbcInsert =
                        new SimpleJdbcInsert(jdbcTemplate).withTableName(LibraryItemEntity.TABLE_NAME);
                int[] rows = songsJdbcInsert.executeBatch(sqlParameterSources);
                log.info("Scanned source: {}. {} files were successfully indexed", mediaSource, rows.length);

                logBuilder.filesScanned(rows.length).scanStatus(DirectoryScanStatus.COMPLETED);

            } catch (DataAccessException e) {
                logBuilder.scanStatus(DirectoryScanStatus.FAILED).scanStatusDetails(e.getMessage());
                log.error("Batch insert failed", e);
            } finally {
                logBuilder.scanDuration(Duration.between(scanStartedAt, LocalTime.now()));
                directoryScanLogDao.save(logBuilder.build());
                clean();
            }
        }
    }

    private void clean() {
        artistIds.clear();
        albumIds.clear();
    }

    private void prepare(ScannerOptions options) {
        if (options.deleteAllBeforeScan()) {
            jdbcTemplate.execute(String.format("TRUNCATE TABLE %s", LibraryItemEntity.TABLE_NAME));
            log.info("Preparing to scan. Library tables truncated.");
        }
    }

    private Set<String> getAlreadyIndexedFiles() {
        String sql = String.format("SELECT %s FROM %s",
                LibraryItemEntity.COL_FILE_LOCATION, LibraryItemEntity.TABLE_NAME);
        return Collections.synchronizedSet(new HashSet<>(jdbcTemplate.queryForList(sql, String.class)));
    }

    private SqlParameterSource[] getSqlParametersSourceFromPath(MediaSource mediaSource, Set<String> indexedFiles) {
        try (Stream<Path> subdirectories = Files.walk(Path.of(mediaSource.path()), 100).parallel()) {
            return subdirectories
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .filter(f -> SUPPORTED_EXTENSIONS.contains(getExtension(f)))
                    .filter(f -> !indexedFiles.remove(f.getAbsolutePath()))
                    .map(audioFileReader::readGracefully)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(this::audioFileToSqlParametersSourceMapper)
                    .toArray(SqlParameterSource[]::new);
        } catch (IOException e) {
            throw MediaSourceException.scanException(mediaSource, e);
        }
    }

    private SqlParameterSource audioFileToSqlParametersSourceMapper(AudioFile audioFile) {
        String artistName = audioFile.getArtistName();
        String albumName = audioFile.getAlbumName();

        String artistId = artistIds.computeIfAbsent(artistName, key -> RandomStringUtils.randomAlphabetic(8));
        Map<String, String> artistAlbums =
                albumIds.computeIfAbsent(artistId, key -> Collections.synchronizedMap(new LinkedHashMap<>()));
        String albumId = artistAlbums.computeIfAbsent(albumName, key -> RandomStringUtils.randomAlphabetic(8));

        return new MapSqlParameterSource()

                .addValue(LibraryItemEntity.COL_ARTIST_ID, artistId)
                .addValue(LibraryItemEntity.COL_ARTIST_NAME, artistName)
                .addValue(LibraryItemEntity.COL_ARTIST_COUNTRY, audioFile.getArtistCountry())

                .addValue(LibraryItemEntity.COL_ALBUM_ID, albumId)
                .addValue(LibraryItemEntity.COL_ALBUM_NAME, audioFile.getAlbumName())
                .addValue(LibraryItemEntity.COL_ALBUM_GENRE, audioFile.getGenre())
                .addValue(LibraryItemEntity.COL_ALBUM_YEAR, audioFile.getYear())

                .addValue(LibraryItemEntity.COL_SONG_NAME, audioFile.getSongName())
                .addValue(LibraryItemEntity.COL_SONG_COMMENT, audioFile.getComment())
                .addValue(LibraryItemEntity.COL_SONG_LYRICS, audioFile.getLyrics())
                .addValue(LibraryItemEntity.COL_SONG_TRACK_ID, audioFile.getTrackId())
                .addValue(LibraryItemEntity.COL_SONG_TRACK_LENGTH, audioFile.getTrackLength())

                .addValue(LibraryItemEntity.COL_AUDIO_BIT_RATE, audioFile.getBitRate())
                .addValue(LibraryItemEntity.COL_AUDIO_CHANNEL_TYPE, audioFile.getChannels())
                .addValue(LibraryItemEntity.COL_AUDIO_ENCODING_TYPE, audioFile.getEncodingType())
                .addValue(LibraryItemEntity.COL_AUDIO_SAMPLE_RATE, audioFile.getSampleRate())

                .addValue(LibraryItemEntity.COL_FILE_NAME, audioFile.getFileName())
                .addValue(LibraryItemEntity.COL_FILE_LOCATION, audioFile.getFileLocation())
                .addValue(LibraryItemEntity.COL_FILE_EXTENSION, audioFile.getFileExtension())

                .addValue(LibraryItemEntity.COL_MISC_IS_FAVORITE, false)
                .addValue(LibraryItemEntity.COL_MISC_PLAY_COUNT, 0)

                .addValue(LibraryItemEntity.COL_CREATED_BY, "system")
                .addValue(LibraryItemEntity.COL_CREATED_DATE, LocalDateTime.now());
    }

    private String getExtension(File f) {
        String name = f.getName().toLowerCase();
        int i = name.lastIndexOf(".");
        return i == -1 ? "" : name.substring(i + 1);
    }

}
