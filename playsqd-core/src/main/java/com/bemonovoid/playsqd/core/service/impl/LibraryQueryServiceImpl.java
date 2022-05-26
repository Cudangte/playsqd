package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.audio.AudioFile;
import com.bemonovoid.playsqd.core.audio.AudioFileReader;
import com.bemonovoid.playsqd.core.dao.AudioTrackDao;
import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.model.Genre;
import com.bemonovoid.playsqd.core.service.AlbumSearchCriteria;
import com.bemonovoid.playsqd.core.service.ArtistSearchCriteria;
import com.bemonovoid.playsqd.core.service.LibraryQueryService;
import com.bemonovoid.playsqd.core.service.PageableResult;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

@Service
class LibraryQueryServiceImpl implements LibraryQueryService {

    private final AudioTrackDao audioTrackDao;
    private final AudioFileReader audioFileReader;

    LibraryQueryServiceImpl(AudioTrackDao audioTrackDao,
                            AudioFileReader audioFileReader) {
        this.audioTrackDao = audioTrackDao;
        this.audioFileReader = audioFileReader;
    }

    @Override
    public PageableResult<ArtistInfo> getArtists(ArtistSearchCriteria searchCriteria) {
        return audioTrackDao.getArtists(searchCriteria);
    }

    @Override
    public PageableResult<AlbumInfo> getAlbums(AlbumSearchCriteria searchCriteria) {
        return audioTrackDao.getAlbums(searchCriteria);
    }

    @Override
    public PageableResult<AudioTrack> getArtistAlbumSongs(String albumId) {
        return null;
    }

    @Override
    public AudioTrack getAudioTrack(long trackId) {
        return audioTrackDao.getTrackById(trackId);
    }

    @Override
    public Optional<AudioTrack> getRandomSongByGenre(String genre) {
        return audioTrackDao.getRandomTrackByGenre(genre);
    }

    @Override
    public Collection<Genre> getGenres() {
        return audioTrackDao.getAllGenres();
    }
}
