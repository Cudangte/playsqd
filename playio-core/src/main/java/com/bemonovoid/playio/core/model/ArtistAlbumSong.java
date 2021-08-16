package com.bemonovoid.playio.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArtistAlbumSong {

    private final Artist artist;
    private final Album album;
    private final Song song;
}
