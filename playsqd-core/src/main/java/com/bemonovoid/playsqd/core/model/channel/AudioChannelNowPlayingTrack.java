package com.bemonovoid.playsqd.core.model.channel;

import java.time.LocalDateTime;

public record AudioChannelNowPlayingTrack(long id,
                                          long channelId,
                                          long audioTrackId,
                                          int audioTrackItemLengthInSec,
                                          LocalDateTime playStartDate) {
}
