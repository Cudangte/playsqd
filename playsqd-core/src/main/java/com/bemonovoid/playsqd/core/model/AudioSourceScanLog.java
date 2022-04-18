package com.bemonovoid.playsqd.core.model;

import java.time.Duration;

public record AudioSourceScanLog(Long id,
                                 long sourceId,
                                 int itemsScanned,
                                 int itemsMissing,
                                 Duration scanDuration) {


    public static AudioSourceScanLog newEntry(long sourceId,
                                              int itemsScanned,
                                              int itemsMissing,
                                              Duration scanDuration) {
        return new AudioSourceScanLog(null, sourceId, itemsScanned, itemsMissing, scanDuration);
    }
}
