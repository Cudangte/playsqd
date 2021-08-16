package com.bemonovoid.playio.core.model;

import java.time.Duration;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class MusicDirectoryScanLog {

    private long id;

    private String scanDirectory;

    private boolean deleteMissing;

    private boolean deleteAllBeforeScan;

    private int filesScanned;

    private int scannedFilesMissing;

    private DirectoryScanStatus scanStatus;

    private String scanStatusDetails;

    private Duration scanDuration;
}
