package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.artwork.ArtworkSource;

public interface AudioTrackEditorService {

    void updateAlbumArtwork(String albumId, ArtworkSource artworkSource);
}
