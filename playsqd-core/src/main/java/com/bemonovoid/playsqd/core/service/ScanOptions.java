package com.bemonovoid.playsqd.core.service;

import java.util.Collection;

public record ScanOptions(boolean deleteMissing, boolean deleteAllBeforeScan, Collection<Long> directoryIds) {
//
//    private boolean deleteMissing;
//    private boolean deleteAllBeforeScan;
//    private Collection<Long> directoryIds;
    public static ScanOptions withDirectoryIds(Collection<Long> directoryIds) {
        return new ScanOptions(false, false, directoryIds);
    }
}
