package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.Genre;
import com.bemonovoid.playsqd.core.model.channel.AudioTrackCountQueryResult;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioTrackEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface AudioTrackRepository extends CrudRepository<AudioTrackEntity, Long> {

    Stream<AudioTrackEntity> findByArtistId(String artistId);

    Optional<AudioTrackEntity> findFirstByAlbumId(String albumId);

    List<AudioTrackEntity> findByAlbumIdOrderByTrackNumberAsc(String albumId);

    boolean existsByAlbumGenreInIgnoreCase(Collection<String> genres);

    Optional<AudioTrackEntity> findRandomTrackByGenre(@Param("genre") String genre);

    /**
     * Named query implementation
     * @param channelId
     * @param genres
     * @return
     */
    Optional<AudioTrackEntity> findRandomGenreTrackNotYetPlayedByChannelId(@Param("channelId") long channelId,
                                                                           @Param("genres") Collection<String> genres);

    /**
     * Named query implementation
     * @param genres
     * @return
     */
    AudioTrackCountQueryResult countByGenreIn(@Param("genres") Collection<String> genres);

    long artistsCount();

    /**
     * Named query
     * @param artistNameLike
     * @return
     */
    long artistsLikeCount(@Param("nameLike") String artistNameLike);

    /**
     * Named query
     * @param nameStartsWith
     * @return
     */
    long artistsStartWithCount(@Param("nameStartsWith") String nameStartsWith);

    long albumsCount();

    long albumsLikeCount(@Param("albumNameLike") String albumNameLike);

    List<ArtistInfo> pageableArtists(@Param("pageSize") int pageSize, @Param("offset") long offset);

    /**
     * Name query
     * @param pageSize
     * @param offset
     * @param artistNameLike
     * @return
     */
    List<ArtistInfo> pageableArtistsLike(@Param("pageSize") int pageSize,
                                         @Param("offset") long offset,
                                         @Param("nameLike") String artistNameLike);

    /**
     * Name query
     * @param pageSize
     * @param offset
     * @param nameStartsWith
     * @return
     */
    List<ArtistInfo> pageableArtistsStartWith(@Param("pageSize") int pageSize,
                                              @Param("offset") long offset,
                                              @Param("nameStartsWith") String nameStartsWith);

    List<AlbumInfo> artistAlbums(@Param("artistId") String artistId);

    List<AlbumInfo> pageableAlbumsLike(@Param("pageSize") int pageSize,
                                       @Param("offset") long offset,
                                       @Param("albumNameLike") String artistNameLike);

    Stream<AudioTrackEntity> getAllByFileLocationStartsWith(String location);

    void deleteAllByFileLocationStartsWith(String location);

    Collection<Genre> findAllGenres();

    @Modifying
    @Query("UPDATE AUDIO_TRACK SET " +
            "ALBUM_ARTWORK_URL = :artworkUrl, LAST_MODIFIED_BY = 'system', LAST_MODIFIED_DATE = :lastModifiedAt " +
            "WHERE ALBUM_ID = :albumId")
    void setAlbumArtworkUrl(@Param("albumId") String albumId, @Param("artworkUrl") String artworkUrl);
}
