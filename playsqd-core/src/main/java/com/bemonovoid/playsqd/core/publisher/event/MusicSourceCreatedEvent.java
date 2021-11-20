package com.bemonovoid.playsqd.core.publisher.event;

import com.bemonovoid.playsqd.core.model.MediaSource;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MusicSourceCreatedEvent extends ApplicationEvent {

    private final MediaSource mediaSource;

    public MusicSourceCreatedEvent(Object source, MediaSource mediaSource) {
        super(source);
        this.mediaSource = mediaSource;
    }
}
