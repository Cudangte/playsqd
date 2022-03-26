package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Album {

    private Artist artist;
    private String id;
    private String name;
    private String year;
    private String genre;

    @JsonProperty("songs_count")
    private int totalSongs;

    @JsonProperty("total_time_in_seconds")
    private int totalTimeInSeconds;
}
