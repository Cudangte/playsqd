package com.bemonovoid.playsqd.core.publisher.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AudioChannelTrackIsNowPlayingEvent extends ApplicationEvent {

    private final long channelId;
    private final long streamedItemId;

    public AudioChannelTrackIsNowPlayingEvent(Object source, long channelId, long streamedItemId) {
        super(source);
        this.channelId = channelId;
        this.streamedItemId = streamedItemId;
    }

}
