package com.bemonovoid.playsqd.core.model;

import lombok.Getter;

@Getter
public class ArtistListItem extends Artist {

    private int totalAlbums;
    private int totalSongs;

    public ArtistListItem(String id, String name, int totalAlbums, int totalSongs) {
        super(id, name);
        this.totalAlbums = totalAlbums;
        this.totalSongs = totalSongs;
    }
}
