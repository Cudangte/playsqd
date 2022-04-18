package com.bemonovoid.playsqd.core.model.channel;

import com.bemonovoid.playsqd.core.model.Song;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record AudioChannelWithStreamingItemInfo(@JsonProperty("channel_id") long channelId,
                                                @JsonProperty("streaming_item") Song streamingItem,
                                                @JsonProperty("streaming_timestamp") LocalDateTime streamingTimestamp) {
}
