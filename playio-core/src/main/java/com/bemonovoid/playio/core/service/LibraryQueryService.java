package com.bemonovoid.playio.core.service;

import java.util.List;

import com.bemonovoid.playio.core.model.Album;
import com.bemonovoid.playio.core.model.Artist;
import com.bemonovoid.playio.core.model.ArtistAlbumSong;
import com.bemonovoid.playio.core.model.ArtistAlbumSongs;

public interface LibraryQueryService {

    PageableResult<Artist> getArtists(PageableRequest pageableRequest);

    PageableResult<Album> getAlbums(LibraryItemFilter libraryItemFilter);

    ArtistAlbumSongs getArtistAlbumSongs(String albumId);

    ArtistAlbumSong getSong(long songId);
}
