package com.bemonovoid.playsqd.core.audio;

import com.bemonovoid.playsqd.core.model.artwork.Artwork;

public interface AudioFile {

    String getArtistName();

    String getArtistCountry();

    String getAlbumName();

    String getAlbumGenre();

    String getAlbumYear();

    int getTrackId();

    String getTrackName();

    int getTrackLength();

    String getComment();

    String getLyrics();

    String getFileName();

    String getFileLocation();

    String getFileExtension();

    String getEncodingType();

    String getSampleRate();

    String getBitRate();

    String getChannels();

    Artwork getArtwork();

}
