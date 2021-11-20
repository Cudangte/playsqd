package com.bemonovoid.playsqd.core.dao;

import com.bemonovoid.playsqd.core.model.Album;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.service.LibraryItemFilter;
import com.bemonovoid.playsqd.core.service.PageableResult;
import com.bemonovoid.playsqd.core.service.PageableSearch;

import java.util.Collection;

public interface MediaLibraryDao {

    PageableResult<ArtistInfo> getArtists(PageableSearch pageableSearch);

    PageableResult<Album> getAlbums(LibraryItemFilter filter);

    PageableResult<Song> getArtistAlbumSongs(String albumId);

    Song getSong(long songId);

    String getSongLocation(long songId);

    String getFirstAlbumSongLocation(String albumId);

    void updateFavoriteStatus(long songId);
}
