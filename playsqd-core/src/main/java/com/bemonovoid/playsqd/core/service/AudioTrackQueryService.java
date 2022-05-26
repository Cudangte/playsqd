package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.artwork.Artwork;
import com.bemonovoid.playsqd.core.model.AudioTrack;

import java.util.Collection;

public interface AudioTrackQueryService {

    Collection<AudioTrack> getAlbumTracks(String albumId);

    Artwork getAlbumArtwork(long trackId);

    Artwork getAlbumArtwork(String albumId);
}
