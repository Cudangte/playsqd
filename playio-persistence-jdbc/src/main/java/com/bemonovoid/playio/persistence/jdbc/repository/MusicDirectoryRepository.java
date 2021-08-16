package com.bemonovoid.playio.persistence.jdbc.repository;

import com.bemonovoid.playio.persistence.jdbc.entity.MusicDirectoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface MusicDirectoryRepository extends CrudRepository<MusicDirectoryEntity, Long> {

    boolean existsByPathEquals(String path);
}
