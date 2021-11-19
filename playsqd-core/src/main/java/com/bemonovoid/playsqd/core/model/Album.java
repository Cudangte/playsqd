package com.bemonovoid.playsqd.core.model;

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
    private int totalSongs;
    private int totalTimeInSeconds;
}
