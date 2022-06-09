package com.bemonovoid.playsqd.core.dao;

import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.model.channel.AudioChannel;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelPlayedTrack;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelState;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelNowPlayingTrack;
import com.bemonovoid.playsqd.core.model.channel.NewAudioChannelData;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AudioChannelDao {

    AudioChannel createChannel(NewAudioChannelData channelData);

    void updateState(long channelId, AudioChannelState newState);

    Optional<AudioChannel> findById(long channelId);

    List<AudioChannel> findAll();

    Collection<AudioChannelPlayedTrack> findPlayedTracks(long channelId);

    AudioChannelNowPlayingTrack createNowPlayingTrack(long channelId, AudioTrack audioTrack);

    AudioChannelNowPlayingTrack updateNowPlayingTrack(long channelId, AudioTrack audioTrack);

    Optional<AudioChannelNowPlayingTrack> findNowPLayingTrackByChannelId(long channelId);

    void setAudioTrackAsPlayed(long channelId, long playedTrackId);

    void deletePlayedTracksByChannelId(long channelId);

    void updatePlayedTrackPlaybackInfo(long channelId, String playbackInfo);

    default AudioChannelNowPlayingTrack findChannelStreamInfoByChannelIdOrThrow(long channelId) {
        return findNowPLayingTrackByChannelId(channelId)
                .orElseThrow(() -> PlayqdException.objectDoesNotExistException(channelId, "AudioChannelStreamInfo"));
    }

    default AudioChannel findChannelByIdOrThrow(long channelId) {
        return findById(channelId)
                .orElseThrow(() -> PlayqdException.objectDoesNotExistException(channelId, "AudioChannel"));
    }
}
