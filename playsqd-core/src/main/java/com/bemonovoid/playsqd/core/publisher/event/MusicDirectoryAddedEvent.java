package com.bemonovoid.playsqd.core.publisher.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MusicDirectoryAddedEvent extends ApplicationEvent {

    private final long directoryId;
    private final boolean autoScan;

    public MusicDirectoryAddedEvent(Object source, long directoryId, boolean autoScan) {
        super(source);
        this.directoryId = directoryId;
        this.autoScan = autoScan;
    }
}
