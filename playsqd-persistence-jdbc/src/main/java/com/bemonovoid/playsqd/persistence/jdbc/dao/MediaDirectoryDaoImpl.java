package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.dao.MediaDirectoryDao;
import com.bemonovoid.playsqd.core.exception.DatabaseItemNotFoundException;
import com.bemonovoid.playsqd.core.model.MediaDirectory;
import com.bemonovoid.playsqd.core.model.MusicDirectoryType;
import com.bemonovoid.playsqd.persistence.jdbc.entity.MediaDirectoryEntity;
import com.bemonovoid.playsqd.persistence.jdbc.repository.MusicDirectoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
class MediaDirectoryDaoImpl implements MediaDirectoryDao {

    private final MusicDirectoryRepository repository;

    MediaDirectoryDaoImpl(MusicDirectoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public MediaDirectory getMediaDirectory(long directoryId) {
        return repository.findById(directoryId)
                .map(this::fromEntity)
                .orElseThrow(() -> new DatabaseItemNotFoundException("Music directory", String.valueOf(directoryId)));
    }

    @Override
    public boolean existsWithPath(String path) {
        return repository.existsByPathEquals(path);
    }

    @Override
    public long add(MediaDirectory mediaDirectory) {
        if (existsWithPath(mediaDirectory.getPath().toString())) {
            throw new InvalidPathException(mediaDirectory.getPath().toString(), "Music directory already exists");
        }
        MediaDirectoryEntity savedEntity = repository.save(MediaDirectoryEntity.builder()
                .type(mediaDirectory.getType().name())
                .path(mediaDirectory.getPath().toString())
                .build());
        return savedEntity.getId();
    }

    @Override
    public Collection<MediaDirectory> getAll() {
        List<MediaDirectory> dirs = new ArrayList<>((int) repository.count());
        repository.findAll().forEach(this::fromEntity);
        return dirs;
    }

    private MediaDirectory fromEntity(MediaDirectoryEntity entity) {
        return MediaDirectory.builder()
                .id(entity.getId())
                .path(Paths.get(entity.getPath()))
                .type(MusicDirectoryType.valueOf(entity.getType()))
                .build();
    }
}
