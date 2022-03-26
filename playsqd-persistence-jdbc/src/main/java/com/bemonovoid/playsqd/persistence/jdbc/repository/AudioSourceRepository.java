package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioSourceEntity;
import org.springframework.data.repository.CrudRepository;

public interface AudioSourceRepository extends CrudRepository<AudioSourceEntity, Long> {

    default AudioSourceEntity findByIdOrThrow(long id) {
        return findById(id).orElseThrow(() -> PlayqdException.objectDoesNotExistException("Source", String.valueOf(id)));
    }

    boolean existsByNameEquals(String name);

    boolean existsByPathEquals(String path);
}
