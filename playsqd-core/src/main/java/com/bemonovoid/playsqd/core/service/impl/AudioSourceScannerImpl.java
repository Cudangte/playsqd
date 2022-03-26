package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.audio.AudioFile;
import com.bemonovoid.playsqd.core.audio.AudioFileReader;
import com.bemonovoid.playsqd.core.dao.AudioSourceDao;
import com.bemonovoid.playsqd.core.dao.MediaLibraryDao;
import com.bemonovoid.playsqd.core.model.AudioSource;
import com.bemonovoid.playsqd.core.model.AudioSourceScanLog;
import com.bemonovoid.playsqd.core.model.ScannableItemInfo;
import com.bemonovoid.playsqd.core.service.AudioSourceScanner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Component
class AudioSourceScannerImpl implements AudioSourceScanner {

    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of("flac", "m4a", "m4p", "mp3", "ogg", "wav", "wma");

    private final Map<String, String> artistIds = Collections.synchronizedMap(new LinkedHashMap<>());
    private final Map<String, Map<String, String>> albumIds = Collections.synchronizedMap(new LinkedHashMap<>());

    private final AudioSourceDao audioSourceDao;
    private final MediaLibraryDao mediaLibraryDao;
    private final AudioFileReader audioFileReader;

    AudioSourceScannerImpl(AudioSourceDao audioSourceDao,
                           MediaLibraryDao mediaLibraryDao,
                           AudioFileReader audioFileReader) {
        this.audioSourceDao = audioSourceDao;
        this.mediaLibraryDao = mediaLibraryDao;
        this.audioFileReader = audioFileReader;
    }

    @Override
    public void scan(long sourceId) {
        scan(audioSourceDao.getById(sourceId));
    }

    @Override
    public void scan(AudioSource audioSource) {
        prepare(audioSource);

        int count = 0;
        LocalTime scanStartedAt = LocalTime.now();
        Set<String> alreadyIndexedLocations = mediaLibraryDao.getAllLocations();

        try (Stream<Path> subdirectories = Files.walk(Path.of(audioSource.path()), 100).parallel()) {
            Stream<ScannableItemInfo> stream = subdirectories
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .filter(f -> SUPPORTED_EXTENSIONS.contains(getExtension(f)))
                    .filter(f -> !alreadyIndexedLocations.remove(f.getAbsolutePath()))
                    .map(audioFileReader::readGracefully)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(this::fromAudioFile);
            count = mediaLibraryDao.addLibraryItems(stream);
            if (count == 0) {
                log.info("Scan completed for source: {}. No files from the source were indexed. " +
                        "Source might have been empty or the source has been indexed already", audioSource);
            } else {
                log.info("Scan completed for source: {}. {} files were successfully indexed", audioSource, count);
            }
        } catch (Exception e) {
            log.error(String.format("Scan failed for source: %s", audioSource), e);
        } finally {
            AudioSourceScanLog scanLog = new AudioSourceScanLog(
                    null,
                    audioSource.path(),
                    count,
                    Duration.between(scanStartedAt, LocalTime.now()));
            audioSourceDao.saveLog(scanLog);
            clean();
        }
    }

    private ScannableItemInfo fromAudioFile(AudioFile audioFile) {
        String artistId =
                artistIds.computeIfAbsent(audioFile.getArtistName(), key -> RandomStringUtils.randomAlphabetic(8));
        Map<String, String> artistAlbums =
                albumIds.computeIfAbsent(artistId, key -> Collections.synchronizedMap(new LinkedHashMap<>()));
        String albumId =
                artistAlbums.computeIfAbsent(audioFile.getAlbumName(), key -> RandomStringUtils.randomAlphabetic(8));
        return new ScannableItemInfo(audioFile, Map.of("artistId", artistId, "albumId", albumId));
    }

    private void prepare(AudioSource audioSource) {
        if (audioSource.deleteAllBeforeScan()) {
            mediaLibraryDao.deleteAllByLocation(audioSource.path());
        }
    }

    private void clean() {
        artistIds.clear();
        albumIds.clear();
    }

    private String getExtension(File f) {
        String name = f.getName().toLowerCase();
        int i = name.lastIndexOf(".");
        return i == -1 ? "" : name.substring(i + 1);
    }
}
