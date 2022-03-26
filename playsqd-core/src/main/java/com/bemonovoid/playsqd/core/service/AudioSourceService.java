package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.AudioSource;

import java.util.Collection;

public interface AudioSourceService {

    Collection<AudioSource> getAll();

    AudioSource getById(long sourceId);

    long create(AudioSource source);

    void update(AudioSource source);

    void delete(long sourceId);
}
