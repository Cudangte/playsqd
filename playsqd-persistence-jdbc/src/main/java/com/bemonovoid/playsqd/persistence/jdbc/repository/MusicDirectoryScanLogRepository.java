package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.persistence.jdbc.entity.MediaSourceScanLogEntity;
import org.springframework.data.repository.CrudRepository;

public interface MusicDirectoryScanLogRepository extends CrudRepository<MediaSourceScanLogEntity, Long> {
}
