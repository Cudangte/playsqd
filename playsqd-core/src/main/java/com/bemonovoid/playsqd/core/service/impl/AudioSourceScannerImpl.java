package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.audio.AudioFile;
import com.bemonovoid.playsqd.core.audio.AudioFileReader;
import com.bemonovoid.playsqd.core.dao.AudioSourceDao;
import com.bemonovoid.playsqd.core.dao.AudioTrackDao;
import com.bemonovoid.playsqd.core.model.AudioSource;
import com.bemonovoid.playsqd.core.model.AudioSourceScanLog;
import com.bemonovoid.playsqd.core.model.ScanStatus;
import com.bemonovoid.playsqd.core.model.ScannedAudioFileMetadata;
import com.bemonovoid.playsqd.core.model.ScannedAudioFileWithMetadata;
import com.bemonovoid.playsqd.core.service.AudioSourceScanner;
import com.bemonovoid.playsqd.core.utils.FileUtils;
import com.bemonovoid.playsqd.core.utils.RandomStrings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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

    private final Map<String, String> artistIds = Collections.synchronizedMap(new LinkedHashMap<>());
    private final Map<String, Map<String, String>> albumIds = Collections.synchronizedMap(new LinkedHashMap<>());

    private final AudioSourceDao audioSourceDao;
    private final AudioTrackDao audioTrackDao;
    private final AudioFileReader audioFileReader;

    AudioSourceScannerImpl(AudioSourceDao audioSourceDao,
                           AudioTrackDao audioTrackDao,
                           AudioFileReader audioFileReader) {
        this.audioSourceDao = audioSourceDao;
        this.audioTrackDao = audioTrackDao;
        this.audioFileReader = audioFileReader;
    }

    @Override
    @Async
    public void scan(long sourceId) {
        scan(audioSourceDao.getById(sourceId));
    }

    @Override
    @Async
    public void scan(AudioSource audioSource) {
        int count = 0;
        boolean scanCompleted = false;
        LocalTime scanStartedAt = LocalTime.now();

        try (Stream<Path> subdirectories = Files.walk(Path.of(audioSource.path()), 100).parallel()) {

            prepare(audioSource);

            Set<String> alreadyIndexedLocations = audioTrackDao.getAllLocations();

            Stream<ScannedAudioFileWithMetadata> stream = subdirectories
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .filter(FileUtils::isSupportedAudioFile)
                    .filter(f -> !alreadyIndexedLocations.remove(f.getAbsolutePath()))
                    .map(audioFileReader::readGracefully)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(this::fromAudioFile);

            count = audioTrackDao.mapToAudioTrackAndSave(stream);

            if (count == 0) {
                log.info("Scan completed for source: {}. No files from the source were indexed. " +
                        "Source might have been empty or the source has been indexed already", audioSource);
            } else {
                log.info("Scan completed for source: {}. {} files were successfully indexed", audioSource, count);
            }
            scanCompleted = true;
        } catch (Exception e) {
            log.error(String.format("Scan failed for source: %s", audioSource), e);
        } finally {
            AudioSourceScanLog scanLog = AudioSourceScanLog.newEntry(
                    audioSource.id(),
                    count,
                    0,
                    Duration.between(scanStartedAt, LocalTime.now()));

            audioSourceDao.saveLog(scanLog);

            ScanStatus status = scanCompleted ? ScanStatus.SCANNED : ScanStatus.FAILED;
            String details = scanCompleted ? "Scan completed" : "Scan failed";
            try {
                audioSourceDao.save(audioSource.withNewStatus(status, details));
            } catch (Exception e) {
                log.error(String.format("Failed to update audio source scan status to %s with details: %s",
                        status, details), e);
            }
            cleanUp();
        }
    }

    private ScannedAudioFileWithMetadata fromAudioFile(AudioFile audioFile) {
        String artistId =
                artistIds.computeIfAbsent(audioFile.getArtistName(), key -> RandomStrings.randomAlphabetic(8));
        Map<String, String> artistAlbums =
                albumIds.computeIfAbsent(artistId, key -> Collections.synchronizedMap(new LinkedHashMap<>()));
        String albumId =
                artistAlbums.computeIfAbsent(audioFile.getAlbumName(), key -> RandomStrings.randomAlphabetic(8));
        return new ScannedAudioFileWithMetadata(audioFile, new ScannedAudioFileMetadata(artistId, albumId));
    }

    private void prepare(AudioSource audioSource) {
        audioSourceDao.save(audioSource.withNewStatus(ScanStatus.IN_PROGRESS, "Scanning ..."));
        if (audioSource.deleteAllBeforeScan()) {
            audioTrackDao.deleteAllByLocation(audioSource.path());
        }
    }

    private void cleanUp() {
        artistIds.clear();
        albumIds.clear();
    }
}
