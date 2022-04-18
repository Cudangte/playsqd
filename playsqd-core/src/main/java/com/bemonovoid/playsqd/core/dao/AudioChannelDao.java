package com.bemonovoid.playsqd.core.dao;

import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.model.channel.AudioChannel;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelPlaybackItem;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelState;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelStreamingInfo;
import com.bemonovoid.playsqd.core.model.channel.NewAudioChannelData;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AudioChannelDao {

    AudioChannel createChannel(NewAudioChannelData channelData);

    void modifyChannelState(long channelId, AudioChannelState newState);

    default AudioChannel findChannelByIdOrThrow(long channelId) {
        return findChannelById(channelId)
                .orElseThrow(() -> PlayqdException.objectDoesNotExistException(channelId, "AudioChannel"));
    }

    Optional<AudioChannel> findChannelById(long channelId);

    List<AudioChannel> findAllChannels();

    Collection<AudioChannelPlaybackItem> finaAllChannelPlaybackSongs(long channelId);

    default AudioChannelStreamingInfo findChannelStreamInfoByChannelIdOrThrow(long channelId) {
        return findChannelStreamInfoByChannelId(channelId)
                .orElseThrow(() -> PlayqdException.objectDoesNotExistException(channelId, "AudioChannelStreamInfo"));
    }

    Optional<AudioChannelStreamingInfo> findChannelStreamInfoByChannelId(long channelId);

    AudioChannelStreamingInfo createChannelStreamInfo(long channelId, Song song);

    AudioChannelStreamingInfo updateChannelStreamInfo(long channelId, Song song);

    void addStreamedItemToHistory(long channelId, long streamedItemId);

    void deletePlaybackHistory(long channelId);
}
