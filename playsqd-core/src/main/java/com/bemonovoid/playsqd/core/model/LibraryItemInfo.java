package com.bemonovoid.playsqd.core.model;

import com.bemonovoid.playsqd.core.audio.AudioFile;

import java.util.Map;

public record LibraryItemInfo(AudioFile item, Map<String, String> metadata) {
}
