package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.core.model.channel.AudioChannelState;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioChannelEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AudioChannelRepository extends CrudRepository<AudioChannelEntity, Long> {

    @Modifying
    @Query("UPDATE AUDIO_CHANNEL SET STATE = :state, LAST_MODIFIED_BY = 'system', LAST_MODIFIED_DATE = :lastModifiedAt " +
            "WHERE ID = :id")
    boolean setNewState(@Param("id") long id,
                        @Param("state") AudioChannelState state,
                        @Param("lastModifiedAt") LocalDateTime lastModifiedAt);

}
