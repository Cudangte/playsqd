package com.bemonovoid.playsqd.persistence.jdbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table(AudioChannelPlayedTrackEntity.TABLE_NAME)
public record AudioChannelPlayedTrackEntity(
        @Id @Column(COL_ID) Long id,
        @Column(COL_AUDIO_CHANNEL_ID) long channelId,
        @Column(COL_AUDIO_TRACK_ID) long audioTrackId,
        @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL) AuditingEntity auditingEntity) {

    static final String TABLE_NAME = "AUDIO_CHANNEL_PLAYED_TRACK";

    static final String COL_ID = "ID";
    static final String COL_AUDIO_CHANNEL_ID = "AUDIO_CHANNEL_ID";
    static final String COL_AUDIO_TRACK_ID = "AUDIO_TRACK_ID";
}
