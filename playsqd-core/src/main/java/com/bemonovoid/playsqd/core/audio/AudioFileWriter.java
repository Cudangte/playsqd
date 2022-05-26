package com.bemonovoid.playsqd.core.audio;

import com.bemonovoid.playsqd.core.model.artwork.Artwork;
import com.bemonovoid.playsqd.core.model.artwork.ArtworkSource;

import java.util.Map;

public interface AudioFileWriter {

    void writeTag(String fileLocation, Map<AudioFileTag, String> data);

    Artwork writeArtwork(String fileLocation, ArtworkSource artworkSource);
}
