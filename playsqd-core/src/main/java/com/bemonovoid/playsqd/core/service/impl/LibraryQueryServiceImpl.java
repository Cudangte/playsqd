package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.audio.AudioFile;
import com.bemonovoid.playsqd.core.audio.AudioFileReader;
import com.bemonovoid.playsqd.core.dao.MusicLibraryItemDao;
import com.bemonovoid.playsqd.core.model.Album;
import com.bemonovoid.playsqd.core.model.ArtistListItem;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.service.LibraryItemFilter;
import com.bemonovoid.playsqd.core.service.LibraryQueryService;
import com.bemonovoid.playsqd.core.service.PageableResult;
import com.bemonovoid.playsqd.core.service.PageableSearchRequest;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

@Service
class LibraryQueryServiceImpl implements LibraryQueryService {

    private final MusicLibraryItemDao musicLibraryItemDao;
    private final AudioFileReader audioFileReader;

    LibraryQueryServiceImpl(MusicLibraryItemDao musicLibraryItemDao,
                            AudioFileReader audioFileReader) {
        this.musicLibraryItemDao = musicLibraryItemDao;
        this.audioFileReader = audioFileReader;
    }

    @Override
    public PageableResult<ArtistListItem> getArtists(PageableSearchRequest pageableSearchRequest) {
        return musicLibraryItemDao.getArtists(pageableSearchRequest);
    }

    @Override
    public PageableResult<Album> getAlbums(LibraryItemFilter libraryItemFilter) {
        return musicLibraryItemDao.getAlbums(libraryItemFilter);
    }

    @Override
    public Collection<Song> getArtistAlbumSongs(String albumId) {
        return musicLibraryItemDao.getArtistAlbumSongs(albumId);
    }

    @Override
    public Song getSong(long songId) {
        return musicLibraryItemDao.getSong(songId);
    }

    @Override
    public Optional<byte[]> getSongArtwork(long songId) {
        String songLocation = musicLibraryItemDao.getSongLocation(songId);
        AudioFile audioFile = audioFileReader.read(Paths.get(songLocation).toFile());
        return audioFile.getArtwork();
    }

    @Override
    public Optional<byte[]> getAlbumArtwork(String albumId) {
        String songLocation = musicLibraryItemDao.getFirstAlbumSongLocation(albumId);
        AudioFile audioFile = audioFileReader.read(Paths.get(songLocation).toFile());
        return audioFile.getArtwork();
    }

    @Override
    public Optional<byte[]> getArtistArtwork(String artistId) {
        return Optional.empty();
    }
}
