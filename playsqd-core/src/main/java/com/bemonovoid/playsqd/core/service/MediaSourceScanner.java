package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.MediaSource;

import java.util.Collection;
import java.util.List;

public interface MediaSourceScanner {

    default void scan(MediaSource mediaSource) {
        scan(List.of(mediaSource));
    }

    default void scan(Collection<MediaSource> mediaSources) {
        scan(mediaSources, ScannerOptions.empty());
    }

    void scan(Collection<MediaSource> mediaSources, ScannerOptions scannerOptions);
}
