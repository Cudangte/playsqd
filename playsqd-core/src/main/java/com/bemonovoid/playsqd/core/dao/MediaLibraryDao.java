package com.bemonovoid.playsqd.core.dao;

import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.LibraryItemInfo;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.service.AlbumSearchCriteria;
import com.bemonovoid.playsqd.core.service.ArtistSearchCriteria;
import com.bemonovoid.playsqd.core.service.PageableResult;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface MediaLibraryDao {

    PageableResult<ArtistInfo> getArtists(ArtistSearchCriteria searchCriteria);

    PageableResult<AlbumInfo> getAlbums(AlbumSearchCriteria searchCriteria);

    PageableResult<Song> getArtistAlbumSongs(String albumId);

    PageableResult<Song> getChannelHistorySongs(long channelId);

    Song getSong(long songId);

    Optional<Song> getRandomSongByGenre(String genre);

    Optional<Song> findRandomGenreSongNotYetStreamedByChannelId(long channelId, String genre);

    boolean existsByAlbumGenreLikeIgnoreCase(String genre);

    String getSongLocation(long songId);

    String getFirstAlbumSongLocation(String albumId);

    void updateFavoriteStatus(long songId);

    Set<String> getAllLocations();

    void deleteAllByLocation(String location);

    int addLibraryItems(Stream<LibraryItemInfo> itemsStream);
}
