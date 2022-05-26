package com.bemonovoid.playsqd.core.model.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Collection;

public record AudioChannelWithPlaybackInfo(
        @JsonUnwrapped AudioChannel audioChannel,
        @JsonProperty("played_tracks") Collection<AudioChannelPlayedTrack> playedTracks,
        @JsonProperty("total_track_count") AudioTrackCountQueryResult totalTrackCount) {
}
