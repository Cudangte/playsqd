package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.Album;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.Song;

import java.util.Collection;
import java.util.Optional;

public interface LibraryQueryService {

    PageableResult<ArtistInfo> getArtists(PageableSearch pageableSearch);

    PageableResult<Album> getAlbums(LibraryItemFilter libraryItemFilter);

    PageableResult<Song> getArtistAlbumSongs(String albumId);

    Song getSong(long songId);

    Optional<byte[]> getSongArtwork(long songId);

    Optional<byte[]> getAlbumArtwork(String albumId);

    Optional<byte[]> getArtistArtwork(String artistId);
}
