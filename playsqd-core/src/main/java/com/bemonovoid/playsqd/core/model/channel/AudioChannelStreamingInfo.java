package com.bemonovoid.playsqd.core.model.channel;

import java.time.LocalDateTime;

public record AudioChannelStreamingInfo(long id,
                                        long channelId,
                                        long streamingItemId,
                                        LocalDateTime streamingTimestamp,
                                        int streamingItemLengthInSec) {
}
