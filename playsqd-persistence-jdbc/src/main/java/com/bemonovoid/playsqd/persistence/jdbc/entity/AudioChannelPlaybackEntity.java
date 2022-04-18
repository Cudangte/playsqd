package com.bemonovoid.playsqd.persistence.jdbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table(AudioChannelPlaybackEntity.TABLE_NAME)
public record AudioChannelPlaybackEntity(@Id @Column(COL_ID) Long id,
                                         @Column(COL_AUDIO_CHANNEL_ID) long channelId,
                                         @Column(COL_STREAMED_ITEM_ID) long streamedItemId,
                                         @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL) AuditingEntity auditingEntity) {

    static final String TABLE_NAME = "AUDIO_CHANNEL_PLAYBACK_HISTORY";

    static final String COL_ID = "ID";
    static final String COL_AUDIO_CHANNEL_ID = "AUDIO_CHANNEL_ID";
    static final String COL_STREAMED_ITEM_ID = "STREAMED_ITEM_ID";
}
