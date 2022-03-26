package com.bemonovoid.playsqd.core.model;

import java.time.Duration;

public record AudioSourceScanLog(Long id,
                                 String scanDirectory,
                                 int filesScanned,
                                 Duration scanDuration) {
}
