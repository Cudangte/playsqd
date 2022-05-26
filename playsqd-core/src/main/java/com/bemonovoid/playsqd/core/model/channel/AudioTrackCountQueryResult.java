package com.bemonovoid.playsqd.core.model.channel;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AudioTrackCountQueryResult(@JsonProperty("count") long count,
                                         @JsonProperty("length_in_seconds") long lengthinseconds) {
}
