package com.bemonovoid.playsqd.core.dao;

import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.Genre;
import com.bemonovoid.playsqd.core.model.ScannedAudioFileWithMetadata;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.model.channel.AudioTrackCountQueryResult;
import com.bemonovoid.playsqd.core.service.AlbumSearchCriteria;
import com.bemonovoid.playsqd.core.service.ArtistSearchCriteria;
import com.bemonovoid.playsqd.core.service.PageableResult;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface AudioTrackDao {

    PageableResult<ArtistInfo> getArtists(ArtistSearchCriteria searchCriteria);

    PageableResult<AlbumInfo> getAlbums(AlbumSearchCriteria searchCriteria);

    List<AudioTrack> getAlbumTracks(String albumId);

    AudioTrackCountQueryResult getCountByGenres(Collection<String> genres);

    AudioTrack getTrackById(long trackId);

    Optional<AudioTrack> getRandomTrackByGenre(String genre);

    Optional<AudioTrack> findRandomGenreTrackNotYetPlayedByChannelId(long channelId, Collection<String> genres);

    boolean existsByAlbumGenreInIgnoreCase(Collection<String> genres);

    AudioTrack getFirstTrackByAlbumId(String albumId);

    Set<String> getAllLocations();

    Collection<Genre> getAllGenres();

    int mapToAudioTrackAndSave(Stream<ScannedAudioFileWithMetadata> scannedAudioFileWithMetadataStream);

    void updateFavoriteStatus(long trackId);

    List<AudioTrack> getAllByFileLocationStartsWith(String location);

    void deleteAllByLocation(String location);

    void updateAlbumArtworkUrl(String albumId, String artworkUrl);
}
