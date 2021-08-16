package com.bemonovoid.playio.core.service.impl;

import com.bemonovoid.playio.core.dao.MusicLibraryItemDao;
import com.bemonovoid.playio.core.model.Album;
import com.bemonovoid.playio.core.model.Artist;
import com.bemonovoid.playio.core.model.ArtistAlbumSong;
import com.bemonovoid.playio.core.model.ArtistAlbumSongs;
import com.bemonovoid.playio.core.service.LibraryItemFilter;
import com.bemonovoid.playio.core.service.LibraryQueryService;
import com.bemonovoid.playio.core.service.PageableRequest;
import com.bemonovoid.playio.core.service.PageableResult;
import org.springframework.stereotype.Service;

@Service
class LibraryQueryServiceImpl implements LibraryQueryService {

    private final MusicLibraryItemDao musicLibraryItemDao;

    LibraryQueryServiceImpl(MusicLibraryItemDao musicLibraryItemDao) {
        this.musicLibraryItemDao = musicLibraryItemDao;
    }

    @Override
    public PageableResult<Artist> getArtists(PageableRequest pageableRequest) {
        return musicLibraryItemDao.getArtists(pageableRequest);
    }

    @Override
    public PageableResult<Album> getAlbums(LibraryItemFilter libraryItemFilter) {
        return musicLibraryItemDao.getAlbums(libraryItemFilter);
    }

    @Override
    public ArtistAlbumSongs getArtistAlbumSongs(String albumId) {
        return musicLibraryItemDao.getArtistAlbumSongs(albumId);
    }

    @Override
    public ArtistAlbumSong getSong(long songId) {
        return musicLibraryItemDao.getSong(songId);
    }
}
