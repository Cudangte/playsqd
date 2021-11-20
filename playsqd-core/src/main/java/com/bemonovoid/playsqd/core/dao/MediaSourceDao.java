package com.bemonovoid.playsqd.core.dao;

import com.bemonovoid.playsqd.core.model.MediaSource;
import com.bemonovoid.playsqd.core.model.NewMediaSource;

import java.util.Collection;

public interface MediaSourceDao {

    boolean existsById(long sourceId);

    boolean existsByName(String name);

    boolean existsByPath(String path);

    MediaSource getById(long sourceId);

    Collection<MediaSource> getAll();

    Collection<MediaSource> getAllByIds(Collection<Long> ids);

    MediaSource create(NewMediaSource newMediaSource);
}
