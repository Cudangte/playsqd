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
@Table(MediaSourceScanLogEntity.TABLE_NAME)
public class MediaSourceScanLogEntity extends AuditableEntity {

    public static final String TABLE_NAME = "MEDIA_SOURCE_SCAN_LOG";

    public static final String COL_ID = "ID";

    public static final String COL_SCAN_DIRECTORY = "SCAN_DIRECTORY";
    public static final String COL_SCAN_DURATION_IN_MILLIS = "SCAN_DURATION_IN_MILLIS";
    public static final String COL_SCAN_STATUS = "SCAN_STATUS";
    public static final String COL_SCAN_STATUS_DETAILS = "SCAN_STATUS_DETAILS";

    public static final String COL_FILES_SCANNED = "FILES_SCANNED";
    public static final String COL_FILES_MISSING = "FILES_MISSING";

    public static final String COL_OPTION_DELETE_MISSING = "OPTION_DELETE_MISSING";
    public static final String COL_OPTION_DELETE_ALL_BEFORE_SCAN = "OPTION_DELETE_ALL_BEFORE_SCAN";

    @Id
    @Column(COL_ID)
    private final long id;

    @Column(COL_SCAN_DIRECTORY)
    private String scanDirectory;

    @Column(COL_OPTION_DELETE_MISSING)
    private boolean deleteMissing;

    @Column(COL_OPTION_DELETE_ALL_BEFORE_SCAN)
    private boolean deleteAllBeforeScan;

    @Column( COL_FILES_SCANNED)
    private int filesScanned;

    @Column(COL_FILES_MISSING)
    private int scannedFilesMissing;

    @Column(COL_SCAN_STATUS)
    private String scanStatus;

    @Column(COL_SCAN_STATUS_DETAILS)
    private String scanStatusDetails;

    @Column(COL_SCAN_DURATION_IN_MILLIS)
    private long scanDurationInMillis;
}
