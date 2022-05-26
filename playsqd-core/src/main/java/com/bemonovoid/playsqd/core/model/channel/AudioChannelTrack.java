package com.bemonovoid.playsqd.core.model.channel;

import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record AudioChannelTrack(@JsonProperty("channel_id") long channelId,
                                @JsonProperty("track") AudioTrack track,
                                @JsonProperty("play_start_date") LocalDateTime playStartDate) {
}
