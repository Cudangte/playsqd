package com.bemonovoid.playio.core.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArtistAlbumSongs {

    private final Artist artist;
    private final Album album;
    private final List<Song> songs;
}
