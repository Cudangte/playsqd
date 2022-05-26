package com.bemonovoid.playsqd.core.model.artwork;

import lombok.Getter;

@Getter
public abstract sealed class AbstractArtworkSource implements ArtworkSource
        permits ArtworkByteArraySource, ArtworkFileSource, ArtworkUrlSource {

    private final String contentType;

    protected AbstractArtworkSource(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void accept(ArtworkSourceVisitor visitor) {
        visitor.visit(this);
    }
}
