package com.bemonovoid.playsqd.core.model;

import com.bemonovoid.playsqd.core.audio.AudioFile;

import java.util.Map;

public record ScannedAudioFileWithMetadata(AudioFile item, ScannedAudioFileMetadata metadata) {
}
