package com.bemonovoid.playsqd.core.audio;

import java.util.Optional;

public interface AudioFile {

    String getArtistName();

    String getArtistCountry();

    String getAlbumName();

    String getSongName();

    String getGenre();

    String getYear();

    String getComment();

    String getLyrics();

    int getTrackId();

    String getFileName();

    String getFileLocation();

    String getFileExtension();

    String getEncodingType();

    String getSampleRate();

    String getBitRate();

    String getChannels();

    int getTrackLength();

    Optional<byte[]> getArtwork();

}
