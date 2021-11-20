package com.bemonovoid.playsqd.core.publisher.listener;

import com.bemonovoid.playsqd.core.publisher.event.MusicSourceCreatedEvent;
import com.bemonovoid.playsqd.core.publisher.event.MusicSourceScanRequestedEvent;
import com.bemonovoid.playsqd.core.service.MediaSourceScanner;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
class MusicScannerEventHandler {

    private final MediaSourceScanner mediaSourcescanner;

    MusicScannerEventHandler(MediaSourceScanner mediaSourcescanner) {
        this.mediaSourcescanner = mediaSourcescanner;
    }

    @Async
    @EventListener(MusicSourceCreatedEvent.class)
    public void handle(MusicSourceCreatedEvent event) {
        mediaSourcescanner.scan(event.getMediaSource());
    }

    @Async
    @EventListener(MusicSourceScanRequestedEvent.class)
    public void handle(MusicSourceScanRequestedEvent event) {
        mediaSourcescanner.scan(event.getMediaSources(), event.getOptions());
    }
}
