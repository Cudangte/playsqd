package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AlbumTracksInfo(@JsonProperty("track _count") int trackCount,
                              @JsonProperty("length_in_seconds") int lengthInSeconds) {
}
