package com.bemonovoid.playio.core.audio;

import java.io.File;
import java.util.Optional;

public interface AudioFileReader {

    Optional<AudioFile> readSilently(File file);

    /**
     *
     * @param file
     * @return
     * @throws AudioFileIOException
     */
    AudioFile read(File file);
}
