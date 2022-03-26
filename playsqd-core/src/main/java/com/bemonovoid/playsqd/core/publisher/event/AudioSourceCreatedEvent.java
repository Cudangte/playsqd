package com.bemonovoid.playsqd.core.publisher.event;

import com.bemonovoid.playsqd.core.model.AudioSource;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AudioSourceCreatedEvent extends ApplicationEvent {

    private final AudioSource audioSource;

    public AudioSourceCreatedEvent(Object source, AudioSource audioSource) {
        super(source);
        this.audioSource = audioSource;
    }
}
