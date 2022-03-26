package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.dao.AudioSourceDao;
import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.AudioSource;
import com.bemonovoid.playsqd.core.model.AudioSourceScanLog;
import com.bemonovoid.playsqd.core.model.AudioSourceScanStatus;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioSourceEntity;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioSourceLogEntity;
import com.bemonovoid.playsqd.persistence.jdbc.repository.AudioSourceRepository;
import com.bemonovoid.playsqd.persistence.jdbc.repository.AudioSourceLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
class AudioSourceDaoImpl implements AudioSourceDao {

    private final AudioSourceRepository repository;
    private final AudioSourceLogRepository logRepository;

    AudioSourceDaoImpl(AudioSourceRepository repository, AudioSourceLogRepository logRepository) {
        this.repository = repository;
        this.logRepository = logRepository;
    }

    @Override
    public boolean existsById(long sourceId) {
        return repository.existsById(sourceId);
    }

    @Override
    public boolean existsByPath(String path) {
        return repository.existsByPathEquals(path);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByNameEquals(name);
    }

    @Override
    public AudioSource getById(long sourceId) {
        return fromEntity(repository.findByIdOrThrow(sourceId));
    }

    @Override
    public AudioSource save(AudioSource audioSource) {
        AudioSourceEntity entity;
        if (audioSource.isNew()) {
            entity = AudioSourceEntity.builder()
                    .name(audioSource.name())
                    .path(audioSource.path())
                    .autoScanOnCreate(audioSource.autoScanOnCreate())
                    .autoScanOnRestart(audioSource.autoScanOnRestart())
                    .deleteAllBeforeScan(audioSource.deleteAllBeforeScan())
                    .deleteMissing(audioSource.deleteMissing())
                    .status(AudioSourceScanStatus.CREATED)
                    .build();
        } else {
            entity = repository.findByIdOrThrow(audioSource.id());
            entity = AudioSourceEntity.builder()
                    .id(entity.getId())
                    .name(!entity.getName().equals(audioSource.name()) ? audioSource.name() : entity.getName())
                    .path(!entity.getPath().equals(audioSource.path()) ? audioSource.path() : entity.getPath())
                    .autoScanOnRestart(audioSource.autoScanOnRestart())
                    .autoScanOnCreate(audioSource.autoScanOnCreate())
                    .deleteAllBeforeScan(audioSource.deleteAllBeforeScan())
                    .deleteMissing(audioSource.deleteMissing())
                    .status(audioSource.status())
                    .statusDetails(audioSource.details())
                    .lastScanDate(audioSource.lastScanDate())
                    .build();
        }
        return fromEntity(repository.save(entity));
    }

    @Override
    public void saveLog(AudioSourceScanLog directoryScanLog) {
        AudioSourceLogEntity entity = AudioSourceLogEntity.builder()
                .sourceDirectory(directoryScanLog.scanDirectory())
                .scanDurationInMillis(directoryScanLog.scanDuration().toMillis())
                .filesScanned(directoryScanLog.filesScanned())
                .build();
        logRepository.save(entity);
    }

    @Override
    public void delete(long sourceId) {
        try {
            repository.deleteById(sourceId);
        } catch (Exception e) {
            throw PlayqdException.genericException(e.getMessage());
        }
    }

    @Override
    public Collection<AudioSource> getAll() {
        Iterator<AudioSourceEntity> iterator = repository.findAll().iterator();
        return Stream.generate(() -> null)
                .takeWhile(x -> iterator.hasNext())
                .map(x -> iterator.next())
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<AudioSource> getAllByIds(Collection<Long> ids) {
        List<AudioSource> sources = new ArrayList<>(ids.size());
        repository.findAllById(ids).forEach(entity -> sources.add(fromEntity(entity)));
        return sources;
    }

    private AudioSource fromEntity(AudioSourceEntity entity) {
        return new AudioSource(
                entity.getId(),
                entity.getName(),
                entity.getPath(),
                entity.isAutoScanOnCreate(),
                entity.isAutoScanOnRestart(),
                entity.isDeleteMissing(),
                entity.isDeleteAllBeforeScan(),
                entity.getStatus(),
                entity.getStatusDetails(),
                entity.getLastScanDate());
    }
}
