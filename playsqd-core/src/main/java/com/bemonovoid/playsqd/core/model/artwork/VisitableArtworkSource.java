package com.bemonovoid.playsqd.core.model.artwork;

public interface VisitableArtworkSource {

    void accept(ArtworkSourceVisitor visitor);
}
