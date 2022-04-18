package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioChannelStreamInfoEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AudioChannelStreamInfoRepository extends CrudRepository<AudioChannelStreamInfoEntity, Long> {

    Optional<AudioChannelStreamInfoEntity> findByChannelId(long channelId);

    @Modifying
    @Query("UPDATE AUDIO_CHANNEL_STREAM_INFO " +
            "SET " +
                "STREAMING_ITEM_ID = :streamingItemId, " +
                "STREAMING_TIMESTAMP = :steamingTimeStamp, " +
                "STREAMING_ITEM_LENGTH_IN_SEC = :streamingItemLengthInSec " +
            "WHERE AUDIO_CHANNEL_ID = :channelId")
    boolean setStreamingItemId(@Param("channelId") long channelId,
                               @Param("streamingItemId") long streamingItemId,
                               @Param("steamingTimeStamp") LocalDateTime steamingTimeStamp,
                               @Param("streamingItemLengthInSec") int streamingItemLengthInSec);
}
