package com.bemonovoid.playsqd.persistence.jdbc.entity;

import com.bemonovoid.playsqd.core.model.channel.AudioChannelSelection;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelState;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table(AudioChannelEntity.TABLE_NAME)
public record AudioChannelEntity(
        @Id @Column(COL_ID) Long id,
        @Column(COL_NAME) String name,
        @Column(COL_DESCRIPTION) String description,
        @Column(COL_STATE) AudioChannelState state,
        @Column(COL_TYPE) AudioChannelType type,
        @Column(COL_REPEAT) boolean repeat,
        @Column(COL_ITEM_SELECTION) AudioChannelSelection itemSelection,
        @Column(COL_SOURCE) String source,
        @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL) AuditingEntity auditingEntity) {

    static final String TABLE_NAME = "AUDIO_CHANNEL";

    static final String COL_ID = "ID";
    static final String COL_NAME = "NAME";
    static final String COL_DESCRIPTION = "DESCRIPTION";
    static final String COL_STATE = "STATE";
    static final String COL_TYPE = "TYPE";
    static final String COL_SOURCE = "SOURCE";
    static final String COL_REPEAT = "REPEAT";
    static final String COL_ITEM_SELECTION = "ITEM_SELECTION";
}
