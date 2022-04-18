package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.channel.AudioChannel;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelPlaybackItem;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelWithStreamingItemInfo;
import com.bemonovoid.playsqd.core.model.channel.NewAudioChannelData;

import java.util.Collection;
import java.util.List;

public interface AudioChannelService {

    AudioChannel create(NewAudioChannelData channelData);

    AudioChannel getChannel(long channelId);

    Collection<AudioChannelPlaybackItem> getChannelPlaybackSongs(long channelId);

    void deleteChannelPlaybackHistory(long channelId);

    AudioChannelWithStreamingItemInfo audioChannelWithStreamingItemInfo(long channelId);

    List<AudioChannel> getAllChannels();
}
