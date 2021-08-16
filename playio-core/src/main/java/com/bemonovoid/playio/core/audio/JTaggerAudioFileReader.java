package com.bemonovoid.playio.core.audio;

import java.io.File;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFileIO;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class JTaggerAudioFileReader implements AudioFileReader {

    @Override
    public Optional<AudioFile> readSilently(File file) {
        try {
            return Optional.of(read(file));
        } catch (AudioFileIOException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public AudioFile read(File file) {
        try {
            return new JTaggerAudioFile(AudioFileIO.read(file));
        } catch (Exception e) {
            String message = String.format("Unable to read AudioFile from: %s", file.getAbsolutePath());
            throw new AudioFileIOException(message, e);
        }
    }
}
