package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.AudioSource;
import com.bemonovoid.playsqd.core.model.AudioSourceWithContent;
import com.bemonovoid.playsqd.core.model.SourceContentItem;

import java.util.Collection;
import java.util.List;

public interface AudioSourceService {

    Collection<AudioSource> getAll();

    AudioSource getById(long sourceId);

    AudioSourceWithContent getAudioSourceWithContentForPath(long sourceId, String path);

    long create(AudioSource source);

    void update(AudioSource source);

    void delete(long sourceId);
}
