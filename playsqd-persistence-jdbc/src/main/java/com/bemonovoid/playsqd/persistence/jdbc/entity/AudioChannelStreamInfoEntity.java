package com.bemonovoid.playsqd.persistence.jdbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(AudioChannelStreamInfoEntity.TABLE_NAME)
public record AudioChannelStreamInfoEntity(@Id @Column(COL_ID) Long id,
                                           @Column(COL_AUDIO_CHANNEL_ID) long channelId,
                                           @Column(COL_STREAMING_ITEM_ID) long streamingItemId,
                                           @Column(COL_STREAMING_TIMESTAMP) LocalDateTime steamingTimeStamp,
                                           @Column(COL_STREAMING_ITEM_LENGTH_IN_SEC) int streamingItemLengthInSec,
                                           @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL) AuditingEntity auditingEntity) {

    static final String TABLE_NAME = "AUDIO_CHANNEL_STREAM_INFO";

    static final String COL_ID = "ID";
    static final String COL_AUDIO_CHANNEL_ID = "AUDIO_CHANNEL_ID";
    static final String COL_STREAMING_ITEM_ID = "STREAMING_ITEM_ID";
    static final String COL_STREAMING_TIMESTAMP = "STREAMING_TIMESTAMP";
    static final String COL_STREAMING_ITEM_LENGTH_IN_SEC = "STREAMING_ITEM_LENGTH_IN_SEC";
}
