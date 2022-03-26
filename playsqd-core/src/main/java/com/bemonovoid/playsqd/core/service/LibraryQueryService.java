package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.Song;

import java.util.Optional;

public interface LibraryQueryService {

    PageableResult<ArtistInfo> getArtists(ArtistSearchCriteria searchCriteria);

    PageableResult<AlbumInfo> getAlbums(AlbumSearchCriteria searchCriteria);

    PageableResult<Song> getArtistAlbumSongs(String albumId);

    Song getSong(long songId);

    Optional<byte[]> getSongArtwork(long songId);

    Optional<byte[]> getAlbumArtwork(String albumId);

    Optional<byte[]> getArtistArtwork(String artistId);
}
