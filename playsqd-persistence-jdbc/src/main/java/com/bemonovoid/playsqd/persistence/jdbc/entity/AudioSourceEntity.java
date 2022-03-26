package com.bemonovoid.playsqd.persistence.jdbc.entity;

import com.bemonovoid.playsqd.core.model.AudioSourceScanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(AudioSourceEntity.TABLE_NAME)
@Getter
@Builder
@AllArgsConstructor(onConstructor=@__({@PersistenceConstructor}))
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AudioSourceEntity extends AuditableEntity {

    public static final String TABLE_NAME = "AUDIO_SOURCE";

    static final String COL_AUTO_SCAN_ON_CREATE = "AUTO_SCAN_ON_CREATE";
    static final String COL_AUTO_SCAN_ON_RESTART = "AUTO_SCAN_ON_RESTART";
    static final String COL_DELETE_MISSING = "DELETE_MISSING";
    static final String COL_DELETE_ALL_BEFORE_SCAN = "DELETE_ALL_BEFORE_SCAN";
    static final String COL_STATUS = "STATUS";
    static final String COL_STATUS_DETAILS = "STATUS_DETAILS";
    static final String COL_LAST_SCAN_DATE = "LAST_SCAN_DATE";

    @Id
    private final long id;

    private final String name;

    private final String path;

    @Column(COL_AUTO_SCAN_ON_CREATE)
    private final boolean autoScanOnCreate;

    @Column(COL_AUTO_SCAN_ON_RESTART)
    private final boolean autoScanOnRestart;

    @Column(COL_DELETE_MISSING)
    private final boolean deleteMissing;

    @Column(COL_DELETE_ALL_BEFORE_SCAN)
    private final boolean deleteAllBeforeScan;

    @Column(COL_STATUS)
    private final AudioSourceScanStatus status;

    @Column(COL_STATUS_DETAILS)
    private final String statusDetails;

    @Column(COL_LAST_SCAN_DATE)
    private final LocalDateTime lastScanDate;

}
