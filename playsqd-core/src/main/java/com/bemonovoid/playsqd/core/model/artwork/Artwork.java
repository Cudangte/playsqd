package com.bemonovoid.playsqd.core.model.artwork;

public record Artwork(byte[] binaryData, String contentType, String imageUrl) {

    public Artwork(org.jaudiotagger.tag.images.Artwork jTaggerArtwork) {
        this(jTaggerArtwork.getBinaryData(), jTaggerArtwork.getMimeType(), jTaggerArtwork.getImageUrl());
    }

    public boolean isEmpty() {
        return !hasBinaryData() && !hasUrl();
    }

    public boolean hasBinaryData() {
        return binaryData != null;
    }

    public boolean hasUrl() {
        return imageUrl != null;
    }

    public static Artwork empty() {
        return new Artwork(null, null, null);
    }
}
