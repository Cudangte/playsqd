package com.bemonovoid.playsqd.core.model.artwork;

import lombok.Getter;

@Getter
public final class ArtworkUrlSource extends AbstractArtworkSource {

    private final String imageUrl;

    public ArtworkUrlSource(String imageUrl) {
        super(null);
        this.imageUrl = imageUrl;
    }

    @Override
    public void accept(ArtworkSourceVisitor visitor) {
        visitor.visit(this);
    }
}
