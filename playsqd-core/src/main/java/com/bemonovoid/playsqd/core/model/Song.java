package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Song {

    private long id;

    private Artist artist;

    private Album album;

    private String name;

    private int trackId;

    @JsonProperty("track_length_in_seconds")
    private int trackLengthInSeconds;

    private String comment;

    private String lyrics;

    @JsonProperty("bit_rate")
    private String audioBitRate;

    @JsonProperty("channel_type")
    private String audioChannelType;

    @JsonProperty("encoding_type")
    private String audioEncodingType;

    @JsonProperty("sample_rate")
    private String audioSampleRate;

    private String fileName;

    @JsonProperty("extension")
    private String fileExtension;

    @JsonIgnore
    private String fileLocation;

    private int playCount;

    private boolean favorite;
}
