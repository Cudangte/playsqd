package com.bemonovoid.playsqd.core.utils;

public enum AudioFileExtension {

    MP3("mp3"),

    MPEG("mpeg"),

    OGA("oga"),

    OGG("ogg"),

    WMA("wma");

    private String value;

    AudioFileExtension(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean valueEqualsIgnoreCase(String that) {
        return this.value.equalsIgnoreCase(that);
    }
}
