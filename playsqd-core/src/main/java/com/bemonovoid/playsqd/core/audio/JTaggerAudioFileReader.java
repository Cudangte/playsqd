package com.bemonovoid.playsqd.core.audio;

import com.bemonovoid.playsqd.core.exception.PlayqdException;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFileIO;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

@Slf4j
@Component
class JTaggerAudioFileReader implements AudioFileReader {

    @Override
    public Optional<AudioFile> readGracefully(File file) {
        try {
            return Optional.of(read(file));
        } catch (PlayqdException e) {
            String message = String.format("JTagger was unable to read AudioFile from: %s", file.getAbsolutePath());
            log.error(message, e);
            return Optional.empty();
        }
    }

    @Override
    public AudioFile read(File file) {
        try {
            return new JTaggerAudioFile(AudioFileIO.read(file));
        } catch (Exception e) {
            String message = String.format("JTagger was unable to read AudioFile from: %s", file.getAbsolutePath());
            log.error(message, e);
            throw PlayqdException.ioException(message, e);
        }
    }

}
