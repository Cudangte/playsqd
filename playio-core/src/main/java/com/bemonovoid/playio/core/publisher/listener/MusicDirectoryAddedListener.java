package com.bemonovoid.playio.core.publisher.listener;

import com.bemonovoid.playio.core.publisher.event.MusicDirectoryAddedEvent;
import com.bemonovoid.playio.core.service.MusicDirectoryScanner;
import com.bemonovoid.playio.core.service.ScanOptions;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MusicDirectoryAddedListener {

    private final MusicDirectoryScanner directoryScanner;

    public MusicDirectoryAddedListener(MusicDirectoryScanner directoryScanner) {
        this.directoryScanner = directoryScanner;
    }

    @Async
    @EventListener(MusicDirectoryAddedEvent.class)
    public void handle(MusicDirectoryAddedEvent event) {
        if (event.isAutoScan()) {
            directoryScanner.scan(ScanOptions.builder().directoryIds(List.of(event.getDirectoryId())).build());
        }
    }
}
