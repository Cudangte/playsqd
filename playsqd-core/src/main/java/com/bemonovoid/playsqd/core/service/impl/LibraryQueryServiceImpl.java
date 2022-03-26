package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.audio.AudioFile;
import com.bemonovoid.playsqd.core.audio.AudioFileReader;
import com.bemonovoid.playsqd.core.dao.MediaLibraryDao;
import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.service.AlbumSearchCriteria;
import com.bemonovoid.playsqd.core.service.ArtistSearchCriteria;
import com.bemonovoid.playsqd.core.service.LibraryQueryService;
import com.bemonovoid.playsqd.core.service.PageableResult;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Optional;

@Service
class LibraryQueryServiceImpl implements LibraryQueryService {

    private final MediaLibraryDao mediaLibraryDao;
    private final AudioFileReader audioFileReader;

    LibraryQueryServiceImpl(MediaLibraryDao mediaLibraryDao,
                            AudioFileReader audioFileReader) {
        this.mediaLibraryDao = mediaLibraryDao;
        this.audioFileReader = audioFileReader;
    }

    @Override
    public PageableResult<ArtistInfo> getArtists(ArtistSearchCriteria searchCriteria) {
        return mediaLibraryDao.getArtists(searchCriteria);
    }

    @Override
    public PageableResult<AlbumInfo> getAlbums(AlbumSearchCriteria searchCriteria) {
        return mediaLibraryDao.getAlbums(searchCriteria);
    }

    @Override
    public PageableResult<Song> getArtistAlbumSongs(String albumId) {
        return mediaLibraryDao.getArtistAlbumSongs(albumId);
    }

    @Override
    public Song getSong(long songId) {
        return mediaLibraryDao.getSong(songId);
    }

    @Override
    public Optional<byte[]> getSongArtwork(long songId) {
        String songLocation = mediaLibraryDao.getSongLocation(songId);
        AudioFile audioFile = audioFileReader.read(Paths.get(songLocation).toFile());
        return audioFile.getArtwork();
    }

    @Override
    public Optional<byte[]> getAlbumArtwork(String albumId) {
        String songLocation = mediaLibraryDao.getFirstAlbumSongLocation(albumId);
        AudioFile audioFile = audioFileReader.read(Paths.get(songLocation).toFile());
        return audioFile.getArtwork();
    }

    @Override
    public Optional<byte[]> getArtistArtwork(String artistId) {
        return Optional.empty();
    }
}
