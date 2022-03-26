package com.bemonovoid.playsqd.core.publisher.event;

import com.bemonovoid.playsqd.core.model.AudioSource;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Collection;

@Getter
public class AudioSourceScanRequestedEvent extends ApplicationEvent {

    private final Collection<AudioSource> audioSources;

    public AudioSourceScanRequestedEvent(Object source, Collection<AudioSource> audioSources) {
        super(source);
        this.audioSources = audioSources;
    }
}
