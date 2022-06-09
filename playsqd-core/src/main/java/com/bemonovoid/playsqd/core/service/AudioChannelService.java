package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.channel.AudioChannel;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelWithPlaybackInfo;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelPlayedTrack;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelTrack;
import com.bemonovoid.playsqd.core.model.channel.NewAudioChannelData;

import java.util.Collection;
import java.util.List;

public interface AudioChannelService {

    AudioChannel create(NewAudioChannelData channelData);

    AudioChannel getChannel(long channelId);

    AudioChannelWithPlaybackInfo getChannelExtended(long channelId);

    Collection<AudioChannelPlayedTrack> getChannelPlayedTracks(long channelId);

    void deleteChannelPlaybackHistory(long channelId);

    AudioChannelTrack audioChannelNowPlayingTrack(long channelId);

    List<AudioChannel> getAllChannels();

    void skipNowPlayingTrack(long channelId, String reason);
}
