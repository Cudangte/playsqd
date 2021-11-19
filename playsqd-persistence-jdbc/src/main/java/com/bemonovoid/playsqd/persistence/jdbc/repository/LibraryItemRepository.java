package com.bemonovoid.playsqd.persistence.jdbc.repository;

import java.util.List;
import java.util.stream.Stream;

import com.bemonovoid.playsqd.persistence.jdbc.entity.LibraryItemEntity;
import org.springframework.data.repository.CrudRepository;

public interface LibraryItemRepository extends CrudRepository<LibraryItemEntity, Long> {

    Stream<LibraryItemEntity> findByArtistId(String artistId);

    List<LibraryItemEntity> findByAlbumId(String albumId);
}
