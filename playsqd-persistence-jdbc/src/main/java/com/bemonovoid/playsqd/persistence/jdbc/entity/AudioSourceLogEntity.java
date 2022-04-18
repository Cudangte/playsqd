package com.bemonovoid.playsqd.persistence.jdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@AllArgsConstructor(onConstructor=@__({@PersistenceConstructor}))
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(AudioSourceLogEntity.TABLE_NAME)
public class AudioSourceLogEntity extends AuditableEntity {

    public static final String TABLE_NAME = "AUDIO_SOURCE_LOG";

    public static final String COL_ID = "ID";
    public static final String COL_SOURCE_ID = "SOURCE_ID";
    public static final String COL_SCAN_DURATION_IN_MILLIS = "SCAN_DURATION_IN_MILLIS";
    public static final String COL_ITEMS_SCANNED = "ITEMS_SCANNED";
    public static final String COL_ITEMS_MISSING = "ITEMS_MISSING";

    @Id
    @Column(COL_ID)
    private final long id;

    @Column(COL_SOURCE_ID)
    private long sourceId;

    @Column( COL_ITEMS_SCANNED)
    private int itemsScanned;

    @Column(COL_ITEMS_MISSING)
    private int itemsMissing;

    @Column(COL_SCAN_DURATION_IN_MILLIS)
    private long scanDurationInMillis;
}
