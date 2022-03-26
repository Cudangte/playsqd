package com.bemonovoid.playsqd.core.service;

import com.bemonovoid.playsqd.core.model.AudioSource;

public interface AudioSourceScanner {

    void scan(long sourceId);

    void scan(AudioSource audioSource);
}
