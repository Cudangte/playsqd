package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.core.model.channel.AudioChannelPlayedTrack;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioChannelPlayedTrackEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface AudioChannelPlayedTrackRepository extends CrudRepository<AudioChannelPlayedTrackEntity, Long> {

    Collection<AudioChannelPlayedTrack> findAudioTracksByChannelId(@Param("channelId") long channelId);

    @Modifying
    @Query("DELETE FROM AUDIO_CHANNEL_PLAYED_TRACK h WHERE h.AUDIO_CHANNEL_ID = :channelId")
    int deleteAllByChannelId(@Param("channelId") long channelId);

    @Modifying
    @Query("UPDATE AUDIO_CHANNEL_PLAYED_TRACK SET AUDIO_TRACK_PLAYBACK_INFO = :playbackInfo, LAST_MODIFIED_BY = 'system', LAST_MODIFIED_DATE = :lastModifiedAt " +
            "WHERE AUDIO_CHANNEL_ID = :channelId AND AUDIO_TRACK_ID = :trackId")
    boolean setPlayedTrackPlaybackInfo(long channelId, long trackId, String playbackInfo);
}
