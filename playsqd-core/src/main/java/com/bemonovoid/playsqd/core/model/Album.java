package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Album(String id,
                    String name,
                    String year,
                    String genre,
                    @JsonProperty("artwork_url") String artworkUrl) {
}
