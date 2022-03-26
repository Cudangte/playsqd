package com.bemonovoid.playsqd.core.dao;

import com.bemonovoid.playsqd.core.model.AudioSource;
import com.bemonovoid.playsqd.core.model.AudioSourceScanLog;

import java.util.Collection;

public interface AudioSourceDao {

    boolean existsById(long sourceId);

    boolean existsByName(String name);

    boolean existsByPath(String path);

    AudioSource getById(long sourceId);

    Collection<AudioSource> getAll();

    Collection<AudioSource> getAllByIds(Collection<Long> ids);

    AudioSource save(AudioSource audioSource);

    void saveLog(AudioSourceScanLog directoryScanLog);

    void delete(long sourceId);
}
