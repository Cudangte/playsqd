package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface LibraryQueryService {

    PageableResult<AlbumInfo> getAlbums(AlbumSearchCriteria searchCriteria);

    PageableResult<AudioTrack> getArtistAlbumSongs(String albumId);

    AudioTrack getAudioTrack(long songId);

    Optional<AudioTrack> getRandomSongByGenre(String genre);

    Collection<Genre> getGenres();
}
