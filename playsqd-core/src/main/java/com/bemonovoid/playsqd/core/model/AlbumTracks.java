package com.bemonovoid.playsqd.core.model;

import java.util.Collection;

public record AlbumTracks(Album album, Collection<AlbumTrack> tracks) {
}
