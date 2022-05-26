package com.bemonovoid.playsqd.core.model.artwork;

import lombok.Getter;

import java.io.File;

@Getter
public final class ArtworkFileSource extends AbstractArtworkSource {

    private final File file;

    public ArtworkFileSource(String contentType, File file) {
        super(contentType);
        this.file = file;
    }

    @Override
    public void accept(ArtworkSourceVisitor visitor) {
        visitor.visit(this);
    }
}
