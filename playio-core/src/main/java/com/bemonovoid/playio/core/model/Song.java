package com.bemonovoid.playio.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Song {

    private long id;

    private String name;

    private int trackId;

    private int trackLengthInSeconds;

    private String comment;

    private String lyrics;

    private String audioBitRate;

    private String audioChannelType;

    private String audioEncodingType;

    private String audioSampleRate;

    private String fileName;

    private String fileExtension;

    @JsonIgnore
    private String fileLocation;

    private int playCount;

    private boolean favorite;
}
