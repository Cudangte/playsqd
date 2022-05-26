package com.bemonovoid.playsqd.core.model.artwork;

import lombok.Getter;

@Getter
public final class ArtworkByteArraySource extends AbstractArtworkSource {

    private final byte[] binaryData;

    public ArtworkByteArraySource(String contentType, byte[] binaryData) {
        super(contentType);
        this.binaryData = binaryData;
    }

    @Override
    public void accept(ArtworkSourceVisitor visitor) {
        visitor.visit(this);
    }
}
