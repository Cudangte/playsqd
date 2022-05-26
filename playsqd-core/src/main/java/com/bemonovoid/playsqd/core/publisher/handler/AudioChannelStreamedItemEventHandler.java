package com.bemonovoid.playsqd.core.publisher.handler;

import com.bemonovoid.playsqd.core.dao.AudioChannelDao;
import com.bemonovoid.playsqd.core.publisher.event.AudioChannelTrackIsNowPlayingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
class AudioChannelStreamedItemEventHandler {

    private final AudioChannelDao audioChannelDao;

    AudioChannelStreamedItemEventHandler(AudioChannelDao audioChannelDao) {
        this.audioChannelDao = audioChannelDao;
    }

    @Async
    @EventListener
    public void handle(AudioChannelTrackIsNowPlayingEvent event) {
        audioChannelDao.setAudioTrackAsPlayed(event.getChannelId(), event.getStreamedItemId());
    }
}
