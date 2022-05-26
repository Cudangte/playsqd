package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.audio.AudioFile;
import com.bemonovoid.playsqd.core.audio.AudioFileReader;
import com.bemonovoid.playsqd.core.dao.AudioTrackDao;
import com.bemonovoid.playsqd.core.model.artwork.Artwork;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.service.AudioTrackQueryService;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Collection;

@Service
record AudioTrackQueryServiceImpl(AudioTrackDao audioTrackDao,
                                  AudioFileReader audioFileReader) implements AudioTrackQueryService {

    @Override
    public Collection<AudioTrack> getAlbumTracks(String albumId) {
        return audioTrackDao.getAlbumTracks(albumId);
    }

    @Override
    public Artwork getAlbumArtwork(long trackId) {
        String audioTrackFileLocation = audioTrackDao.getTrackById(trackId).fileLocation();
        AudioFile audioFile = audioFileReader.read(Paths.get(audioTrackFileLocation).toFile());
        return audioFile.getArtwork();
    }

    @Override
    public Artwork getAlbumArtwork(String albumId) {
        String fileLocation = audioTrackDao.getFirstTrackByAlbumId(albumId).fileLocation();
        AudioFile audioFile = audioFileReader.read(Paths.get(fileLocation).toFile());
        return audioFile.getArtwork();
    }
}
