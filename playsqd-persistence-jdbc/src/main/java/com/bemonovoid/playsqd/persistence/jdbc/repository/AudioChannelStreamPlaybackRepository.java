package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.core.model.channel.AudioChannelPlaybackItem;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioChannelPlaybackEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface AudioChannelStreamPlaybackRepository extends CrudRepository<AudioChannelPlaybackEntity, Long> {

    Collection<AudioChannelPlaybackItem> finaAllChannelPlaybackSongs(@Param("channelId") long channelId);

    @Modifying
    @Query("DELETE FROM AUDIO_CHANNEL_PLAYBACK_HISTORY h WHERE h.AUDIO_CHANNEL_ID = :channelId")
    int deleteAllByChannelId(@Param("channelId") long channelId);
}
