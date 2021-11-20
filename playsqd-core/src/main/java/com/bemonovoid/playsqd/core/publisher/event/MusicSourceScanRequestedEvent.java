package com.bemonovoid.playsqd.core.publisher.event;

import com.bemonovoid.playsqd.core.model.MediaSource;
import com.bemonovoid.playsqd.core.service.ScannerOptions;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Collection;

@Getter
public class MusicSourceScanRequestedEvent extends ApplicationEvent {

    private final Collection<MediaSource> mediaSources;
    private final ScannerOptions options;

    public MusicSourceScanRequestedEvent(Object source, Collection<MediaSource> mediaSources, ScannerOptions options) {
        super(source);
        this.mediaSources = mediaSources;
        this.options = options;
    }
}
