package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.dao.AudioSourceDao;
import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.AudioSource;
import com.bemonovoid.playsqd.core.publisher.event.AudioSourceCreatedEvent;
import com.bemonovoid.playsqd.core.service.AudioSourceService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Collection;

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

        if (source.autoScanOnCreate()) {
            eventPublisher.publishEvent(new AudioSourceCreatedEvent(this, audioSource));
        }

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
