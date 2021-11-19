package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.persistence.jdbc.entity.MediaDirectoryScanLogEntity;
import org.springframework.data.repository.CrudRepository;

public interface MusicDirectoryScanLogRepository extends CrudRepository<MediaDirectoryScanLogEntity, Long> {
}
