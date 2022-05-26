package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ArtistInfo(String id,
                         String name,
                         @JsonProperty("albums_count") int albumscount,
                         @JsonProperty("songs_count") int trackcount) {
}
