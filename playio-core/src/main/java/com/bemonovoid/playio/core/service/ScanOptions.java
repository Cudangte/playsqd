package com.bemonovoid.playio.core.service;

import java.util.Collection;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScanOptions {

    private boolean deleteMissing;
    private boolean deleteAllBeforeScan;
    private Collection<Long> directoryIds;
}
