package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.ScanStatus;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioSourceEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AudioSourceRepository extends CrudRepository<AudioSourceEntity, Long> {

    default AudioSourceEntity findByIdOrThrow(long id) {
        return findById(id).orElseThrow(() -> PlayqdException.objectDoesNotExistException("Source", String.valueOf(id)));
    }

    boolean existsByNameEquals(String name);

    boolean existsByPathEquals(String path);

//    @Modifying
//    @Query("UPDATE AUDIO_SOURCE SET STATUS = :status, STATUS_DETAILS =: details WHERE ID = :id")
//    boolean updateStatus(@Param("id") long id, @Param("status") ScanStatus status, @Param("details") String details);
}
