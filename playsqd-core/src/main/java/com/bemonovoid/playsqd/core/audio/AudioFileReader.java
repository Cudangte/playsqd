package com.bemonovoid.playsqd.core.audio;

import java.io.File;
import java.util.Optional;

public interface AudioFileReader {

    Optional<AudioFile> readGracefully(File file);

    AudioFile read(File file);
}
