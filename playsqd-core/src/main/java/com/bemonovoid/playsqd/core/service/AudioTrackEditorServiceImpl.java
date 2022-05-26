package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.audio.AudioFileWriter;
import com.bemonovoid.playsqd.core.dao.AudioTrackDao;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.model.artwork.Artwork;
import com.bemonovoid.playsqd.core.model.artwork.ArtworkSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public record AudioTrackEditorServiceImpl(AudioTrackDao audioTrackDao, AudioFileWriter audioFileWriter)
        implements AudioTrackEditorService {

    @Override
    public void updateAlbumArtwork(String albumId, ArtworkSource artworkSource) {
        List<AudioTrack> albumTracks = audioTrackDao.getAlbumTracks(albumId);
        if (albumTracks.isEmpty()) {
            return;
        }
        List<Artwork> artworks = albumTracks.stream()
                .map(audioTrack -> updateAlbumArtwork(audioTrack, artworkSource))
                .toList();
        if (!artworks.isEmpty()) {
            Artwork artwork = artworks.get(0);
            if (artwork.hasUrl()) {
                audioTrackDao.updateAlbumArtworkUrl(albumId, artwork.imageUrl());
            }
        }
    }

    private Artwork updateAlbumArtwork(AudioTrack audioTrack, ArtworkSource artworkSource) {
        return audioFileWriter.writeArtwork(audioTrack.fileLocation(), artworkSource);
    }
}
