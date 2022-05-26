package com.bemonovoid.playsqd.core.model.artwork;

import com.bemonovoid.playsqd.core.exception.PlayqdException;

public interface ArtworkSourceVisitor {

    default void visit(ArtworkSource artworkSource) {
        throw PlayqdException.genericException("Visitor not yet implemented");
    }

    default void visit(AbstractArtworkSource artworkSource) {
        visit((ArtworkSource) artworkSource);
    }

    default void visit(ArtworkFileSource artworkSource) {
        visit((AbstractArtworkSource) artworkSource);
    }

    default void visit(ArtworkByteArraySource artworkSource) {
        visit((AbstractArtworkSource) artworkSource);
    }

    default void visit(ArtworkUrlSource artworkSource) {
        visit((AbstractArtworkSource) artworkSource);
    }
}
