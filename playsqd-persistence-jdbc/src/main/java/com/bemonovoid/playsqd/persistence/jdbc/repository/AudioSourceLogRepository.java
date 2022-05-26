package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioSourceEventEntity;
import org.springframework.data.repository.CrudRepository;

public interface AudioSourceLogRepository extends CrudRepository<AudioSourceEventEntity, Long> {
}
