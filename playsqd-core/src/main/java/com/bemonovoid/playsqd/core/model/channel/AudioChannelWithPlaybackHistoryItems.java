package com.bemonovoid.playsqd.core.model.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Collection;

public record AudioChannelWithPlaybackHistoryItems(
        @JsonUnwrapped AudioChannel audioChannel,
        @JsonProperty("playback_history_items") Collection<AudioChannelPlaybackItem> playbackItems) {
}
