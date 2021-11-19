package com.bemonovoid.playsqd.core.dao;

import com.bemonovoid.playsqd.core.model.Album;
import com.bemonovoid.playsqd.core.model.ArtistListItem;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.service.LibraryItemFilter;
import com.bemonovoid.playsqd.core.service.PageableResult;
import com.bemonovoid.playsqd.core.service.PageableSearchRequest;

import java.util.Collection;

public interface MusicLibraryItemDao {

    PageableResult<ArtistListItem> getArtists(PageableSearchRequest pageableSearchRequest);

    PageableResult<Album> getAlbums(LibraryItemFilter filter);

    Collection<Song> getArtistAlbumSongs(String albumId);

    Song getSong(long songId);

    String getSongLocation(long songId);

    String getFirstAlbumSongLocation(String albumId);

    void updateFavoriteStatus(long songId);
}
