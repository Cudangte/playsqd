package com.bemonovoid.playsqd.core.model.channel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record AudioChannelPlayedTrack(long id,
                                      String name,
                                      @JsonProperty("length_in_seconds") int lengthinseconds,
                                      boolean favorite,
                                      @JsonProperty("playback_timestamp") LocalDateTime playbacktimestamp,
                                      @JsonProperty("artist_id") String artistid,
                                      @JsonProperty("artist_name") String artistname,
                                      @JsonProperty("album_id") String albumid,
                                      @JsonProperty("album_name") String albumname) {
}
