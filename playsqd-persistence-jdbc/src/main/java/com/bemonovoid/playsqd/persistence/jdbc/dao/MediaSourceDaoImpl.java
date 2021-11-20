package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.dao.MediaSourceDao;
import com.bemonovoid.playsqd.core.exception.DatabaseItemNotFoundException;
import com.bemonovoid.playsqd.core.model.MediaSource;
import com.bemonovoid.playsqd.core.model.NewMediaSource;
import com.bemonovoid.playsqd.persistence.jdbc.entity.MediaSourceEntity;
import com.bemonovoid.playsqd.persistence.jdbc.repository.MusicDirectoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
class MediaSourceDaoImpl implements MediaSourceDao {

    private final MusicDirectoryRepository repository;

    MediaSourceDaoImpl(MusicDirectoryRepository repository) {
        this.repository = repository;
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
    public MediaSource getById(long sourceId) {
        return repository.findById(sourceId)
                .map(this::fromEntity)
                .orElseThrow(() -> new DatabaseItemNotFoundException("Media directory", String.valueOf(sourceId)));
    }

    @Override
    public MediaSource create(NewMediaSource newMediaSource) {
        MediaSourceEntity entity = MediaSourceEntity.builder()
                .name(newMediaSource.name())
                .path(newMediaSource.path())
                .autoScanOnRestart(newMediaSource.autoScanOnRestart())
                .build();
        return fromEntity(repository.save(entity));
    }

    @Override
    public Collection<MediaSource> getAll() {
        List<MediaSource> sources = new ArrayList<>((int) repository.count());
        repository.findAll().forEach(entity -> sources.add(fromEntity(entity)));
        return sources;
    }

    @Override
    public Collection<MediaSource> getAllByIds(Collection<Long> ids) {
        List<MediaSource> sources = new ArrayList<>(ids.size());
        repository.findAllById(ids).forEach(entity -> sources.add(fromEntity(entity)));
        return sources;
    }

    private MediaSource fromEntity(MediaSourceEntity entity) {
        return new MediaSource(entity.getId(), entity.getName(), entity.getPath(), entity.isAutoScanOnRestart());
    }
}
