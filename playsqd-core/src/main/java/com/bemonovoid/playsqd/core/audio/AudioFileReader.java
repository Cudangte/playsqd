package com.bemonovoid.playsqd.core.audio;

import java.io.File;
import java.util.Optional;

public interface AudioFileReader {

    default Optional<AudioFile> readGracefully(String path) {
        return readGracefully(new File(path));
    }

    Optional<AudioFile> readGracefully(File file);

    AudioFile read(File file);
}
