package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.NewMediaSource;

import java.util.Collection;

public interface MediaSourceService {

    void scanSources(Collection<Long> sourceIds, ScannerOptions scannerOptions);

    long createSource(NewMediaSource newMediaSource);
}
