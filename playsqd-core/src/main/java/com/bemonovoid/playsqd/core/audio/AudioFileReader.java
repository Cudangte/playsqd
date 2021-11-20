package com.bemonovoid.playsqd.core.audio;

import java.io.File;
import java.util.Optional;

public interface AudioFileReader {

    Optional<AudioFile> readGracefully(File file);

    /**
     *
     * @param file
     * @return
     * @throws AudioFileIOException
     */
    AudioFile read(File file);
}
