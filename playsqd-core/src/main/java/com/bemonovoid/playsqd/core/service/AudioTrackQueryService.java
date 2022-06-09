package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.artwork.Artwork;
import com.bemonovoid.playsqd.core.model.AudioTrack;

import java.util.Collection;

public interface AudioTrackQueryService {

    PageableResult<ArtistInfo> getArtists(ArtistSearchCriteria searchCriteria);

    Collection<AudioTrack> getAlbumTracks(String albumId);

    Artwork getAlbumArtwork(long trackId);

    Artwork getAlbumArtwork(String albumId);
}
