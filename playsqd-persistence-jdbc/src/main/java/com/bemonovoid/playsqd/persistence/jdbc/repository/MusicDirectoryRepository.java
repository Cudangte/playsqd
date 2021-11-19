package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.persistence.jdbc.entity.MediaDirectoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface MusicDirectoryRepository extends CrudRepository<MediaDirectoryEntity, Long> {

    boolean existsByPathEquals(String path);
}
