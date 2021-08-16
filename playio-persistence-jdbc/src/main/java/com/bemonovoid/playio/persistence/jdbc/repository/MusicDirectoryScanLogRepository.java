package com.bemonovoid.playio.persistence.jdbc.repository;

import com.bemonovoid.playio.persistence.jdbc.entity.MusicDirectoryScanLogEntity;
import org.springframework.data.repository.CrudRepository;

public interface MusicDirectoryScanLogRepository extends CrudRepository<MusicDirectoryScanLogEntity, Long> {
}
