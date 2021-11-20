package com.bemonovoid.playsqd.core.exception;

import com.bemonovoid.playsqd.core.model.MediaSource;
import com.bemonovoid.playsqd.core.model.NewMediaSource;

public class MediaSourceException extends PlaysqdException {

    private MediaSourceException(String message) {
        super(message);
    }

    private MediaSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public static MediaSourceException ioException(NewMediaSource newMediaSource, Throwable t) {
        return new MediaSourceException(String.format("Failed to create new media source: '%s'", newMediaSource), t);
    }

    public static MediaSourceException scanException(MediaSource mediaSource, Throwable throwable) {
        String message = String.format("Scanning '%s' media source failed", mediaSource);
        return new MediaSourceException(message, throwable);
    }

    public static MediaSourceException alreadyExists(String identityKey, String value) {
        return new MediaSourceException(
                String.format("Media source with the %s = %s already exists", identityKey, value));
    }
}
