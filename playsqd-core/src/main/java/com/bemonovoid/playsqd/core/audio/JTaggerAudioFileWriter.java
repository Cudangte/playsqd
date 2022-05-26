package com.bemonovoid.playsqd.core.audio;

import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.artwork.Artwork;
import com.bemonovoid.playsqd.core.model.artwork.ArtworkSource;
import com.bemonovoid.playsqd.core.model.artwork.JTaggerArtworkSourceVisitor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

@Slf4j
@Component
public record JTaggerAudioFileWriter() implements AudioFileWriter {

    @Override
    public void writeTag(String fileLocation, Map<AudioFileTag, String> data) {

    }

    @Override
    public Artwork writeArtwork(String fileLocation, ArtworkSource artworkSource) {
        try {
            var artworkSourceVisitor = new JTaggerArtworkSourceVisitor();
            artworkSource.accept(artworkSourceVisitor);
            org.jaudiotagger.tag.images.Artwork artwork = artworkSourceVisitor.getArtwork();

            AudioFile audioFile = AudioFileIO.read(new File(fileLocation));

            audioFile.getTag().setField(artwork);
            AudioFileIO.write(audioFile);

            return new Artwork(artwork);

        } catch (Exception e) {
            throw PlayqdException.ioException("", e);
        }
    }
}
