package com.bemonovoid.playsqd.core.dao;

import com.bemonovoid.playsqd.core.audio.AudioFile;
import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.ScannableItemInfo;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.service.AlbumSearchCriteria;
import com.bemonovoid.playsqd.core.service.ArtistSearchCriteria;
import com.bemonovoid.playsqd.core.service.PageableResult;
import com.bemonovoid.playsqd.core.service.PageableSearch;

import java.util.Set;
import java.util.stream.Stream;

public interface MediaLibraryDao {

    PageableResult<ArtistInfo> getArtists(ArtistSearchCriteria searchCriteria);

    PageableResult<AlbumInfo> getAlbums(AlbumSearchCriteria searchCriteria);

    PageableResult<Song> getArtistAlbumSongs(String albumId);

    Song getSong(long songId);

    String getSongLocation(long songId);

    String getFirstAlbumSongLocation(String albumId);

    void updateFavoriteStatus(long songId);

    Set<String> getAllLocations();

    void deleteAllByLocation(String location);

    int addLibraryItems(Stream<ScannableItemInfo> itemsStream);
}
