package com.bemonovoid.playio.core.dao;

import com.bemonovoid.playio.core.model.Album;
import com.bemonovoid.playio.core.model.Artist;
import com.bemonovoid.playio.core.model.ArtistAlbumSong;
import com.bemonovoid.playio.core.model.ArtistAlbumSongs;
import com.bemonovoid.playio.core.service.LibraryItemFilter;
import com.bemonovoid.playio.core.service.PageableRequest;
import com.bemonovoid.playio.core.service.PageableResult;

public interface MusicLibraryItemDao {

    PageableResult<Artist> getArtists(PageableRequest pageableRequest);

    PageableResult<Album> getAlbums(LibraryItemFilter filter);

    ArtistAlbumSongs getArtistAlbumSongs(String albumId);

    ArtistAlbumSong getSong(long songId);
}
