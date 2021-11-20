package com.bemonovoid.playsqd.core.service;

public record ScannerOptions(boolean deleteMissing, boolean deleteAllBeforeScan) {

    public static ScannerOptions empty() {
        return new ScannerOptions(false, false);
    }
}
