package com.bemonovoid.playsqd.core.publisher.handler;

import com.bemonovoid.playsqd.core.publisher.event.AudioSourceCreatedEvent;
import com.bemonovoid.playsqd.core.service.AudioSourceScanner;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class AudioSourceCreatedEventHandler {

    private final AudioSourceScanner audioSourceScanner;

    AudioSourceCreatedEventHandler(AudioSourceScanner audioSourceScanner) {
        this.audioSourceScanner = audioSourceScanner;
    }

    @EventListener
    public void handleEvent(AudioSourceCreatedEvent event) {
        audioSourceScanner.scan(event.getAudioSource());
    }
}
