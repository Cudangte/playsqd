package com.bemonovoid.playio.persistence.jdbc.dao;

import com.bemonovoid.playio.core.dao.MusicDirectoryDao;
import com.bemonovoid.playio.core.exception.DatabaseItemNotFoundException;
import com.bemonovoid.playio.core.model.MusicDirectory;
import com.bemonovoid.playio.core.model.MusicDirectoryType;
import com.bemonovoid.playio.persistence.jdbc.entity.MusicDirectoryEntity;
import com.bemonovoid.playio.persistence.jdbc.repository.MusicDirectoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
class MusicDirectoryDaoImpl implements MusicDirectoryDao {

    private final MusicDirectoryRepository repository;

    MusicDirectoryDaoImpl(MusicDirectoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public MusicDirectory getMusicDirectory(long directoryId) {
        return repository.findById(directoryId)
                .map(this::fromEntity)
                .orElseThrow(() -> new DatabaseItemNotFoundException("Music directory", String.valueOf(directoryId)));
    }

    @Override
    public boolean existsWithPath(String path) {
        return repository.existsByPathEquals(path);
    }

    @Override
    public long add(MusicDirectory musicDirectory) {
        if (existsWithPath(musicDirectory.getPath().toString())) {
            throw new InvalidPathException(musicDirectory.getPath().toString(), "Music directory already exists");
        }
        MusicDirectoryEntity savedEntity = repository.save(MusicDirectoryEntity.builder()
                .type(musicDirectory.getType().name())
                .path(musicDirectory.getPath().toString())
                .build());
        return savedEntity.getId();
    }

    @Override
    public Collection<MusicDirectory> getAll() {
        List<MusicDirectory> dirs = new ArrayList<>((int) repository.count());
        repository.findAll().forEach(this::fromEntity);
        return dirs;
    }

    private MusicDirectory fromEntity(MusicDirectoryEntity entity) {
        return MusicDirectory.builder()
                .id(entity.getId())
                .path(Paths.get(entity.getPath()))
                .type(MusicDirectoryType.valueOf(entity.getType()))
                .build();
    }
}
