package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.dao.AudioSourceDao;
import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.AudioSource;
import com.bemonovoid.playsqd.core.model.SourceContentItem;
import com.bemonovoid.playsqd.core.publisher.event.AudioSourceCreatedEvent;
import com.bemonovoid.playsqd.core.service.AudioSourceService;
import com.bemonovoid.playsqd.core.utils.FileUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
class AudioSourceServiceImpl implements AudioSourceService {

    private final AudioSourceDao audioSourceDao;
    private final ApplicationEventPublisher eventPublisher;

    AudioSourceServiceImpl(AudioSourceDao audioSourceDao, ApplicationEventPublisher eventPublisher) {
        this.audioSourceDao = audioSourceDao;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Collection<AudioSource> getAll() {
        return audioSourceDao.getAll();
    }

    @Override
    public long create(AudioSource source) {
        String path = source.path();

        validatePath(path);

        if (audioSourceDao.existsByPath(path)) {
            throw PlayqdException.objectAlreadyExistsException(
                    String.format("Audio source with the same path: '%s' already exist", path));
        }

        AudioSource audioSource = audioSourceDao.save(source);

        eventPublisher.publishEvent(new AudioSourceCreatedEvent(this, audioSource));

        return audioSource.id();
    }

    @Override
    public void update(AudioSource source) {
        validatePath(source.path());
        audioSourceDao.save(source);
    }

    @Override
    public void delete(long sourceId) {
        audioSourceDao.delete(sourceId);
    }

    @Override
    public AudioSource getById(long sourceId) {
        return audioSourceDao.getById(sourceId);
    }

    @Override
    public List<SourceContentItem> getFoldersInSourcePath(long sourceId, String path) {
        AudioSource source = getById(sourceId);
        Path lookUpPath = Path.of(source.path(), path != null ? path : "");
        try (Stream<Path> files = Files.list(lookUpPath)) {
            return files
                    .map(Path::toFile)
                    .filter(f -> f.isDirectory() || (f.isFile() && FileUtils.isSupportedAudioFile(f)))
                    .map(AudioSourceServiceImpl::fromFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw PlayqdException.ioException("Failed to read from path", e);
        }
    }

    private static SourceContentItem fromFile(File file) {
        if (file.isFile()) {
            var fileNameExtension = FileUtils.parseFileNameAndExtension(file.getName());
            return new SourceContentItem(
                    fileNameExtension.left(), fileNameExtension.right(), true);
        }
        return new SourceContentItem(file.getName(), null, false);
    }

    private static void validatePath(String path) {
        try {
            if (!Files.exists(Paths.get(path))) {
                throw PlayqdException.ioException(String.format("Audio source for '%s' path does not exist", path), 400);
            }
        } catch (InvalidPathException e) {
            throw PlayqdException.ioException("Audio source path is invalid", 400);
        }
    }
}
