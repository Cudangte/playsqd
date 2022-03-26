package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.persistence.jdbc.entity.LibraryItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface LibraryItemRepository extends CrudRepository<LibraryItemEntity, Long> {

    Stream<LibraryItemEntity> findByArtistId(String artistId);

    List<LibraryItemEntity> findByAlbumId(String albumId);

    Optional<String> findAlbumSongLocation(@Param("albumId") String albumId);

    long artistsCount();

    long artistsLikeCount(@Param("artistNameLike") String artistNameLike);

    long albumsCount();

    long albumsLikeCount(@Param("albumNameLike") String albumNameLike);

    List<ArtistInfo> pageableArtists(@Param("pageSize") int pageSize, @Param("offset") long offset);

    List<ArtistInfo> pageableArtistsLike(@Param("pageSize") int pageSize,
                                         @Param("offset") long offset,
                                         @Param("artistNameLike") String artistNameLike);

    List<AlbumInfo> artistAlbums(@Param("artistId") String artistId);

    List<AlbumInfo> pageableAlbumsLike(@Param("pageSize") int pageSize,
                                       @Param("offset") long offset,
                                       @Param("albumNameLike") String artistNameLike);

    void deleteAllByFileLocationStartsWith(String location);


}
