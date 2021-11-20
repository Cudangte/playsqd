package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.persistence.jdbc.entity.MediaSourceEntity;
import org.springframework.data.repository.CrudRepository;

public interface MusicDirectoryRepository extends CrudRepository<MediaSourceEntity, Long> {

    boolean existsByNameEquals(String name);

    boolean existsByPathEquals(String path);
}
