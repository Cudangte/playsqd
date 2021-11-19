package com.bemonovoid.playsqd.core.publisher.listener;

import com.bemonovoid.playsqd.core.publisher.event.MusicDirectoryAddedEvent;
import com.bemonovoid.playsqd.core.service.MediaDirectoryScanner;
import com.bemonovoid.playsqd.core.service.ScanOptions;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class MusicDirectoryAddedListener {

    private final MediaDirectoryScanner directoryScanner;

    MusicDirectoryAddedListener(MediaDirectoryScanner directoryScanner) {
        this.directoryScanner = directoryScanner;
    }

    @Async
    @EventListener(MusicDirectoryAddedEvent.class)
    public void handle(MusicDirectoryAddedEvent event) {
        if (event.isAutoScan()) {
            directoryScanner.scan(ScanOptions.withDirectoryIds(List.of(event.getDirectoryId())));
        }
    }
}
