package com.bemonovoid.playsqd.core.model.artwork;

import com.bemonovoid.playsqd.core.exception.PlayqdException;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.StandardArtwork;
import org.jaudiotagger.tag.reference.PictureTypes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class JTaggerArtworkSourceVisitor implements ArtworkSourceVisitor {

    private Artwork artwork;

    @Override
    public void visit(AbstractArtworkSource artworkSource) {
        if (artworkSource.getContentType() != null) {
            this.artwork.setMimeType(artworkSource.getContentType());
        }
    }

    @Override
    public void visit(ArtworkFileSource artworkSource) {
        try {
            this.artwork = StandardArtwork.createArtworkFromFile(artworkSource.getFile());
            visit((AbstractArtworkSource) artworkSource);
        } catch (IOException e) {
            throw PlayqdException.ioException("", e);
        }
    }

    @Override
    public void visit(ArtworkUrlSource artworkSource) {
        try {
            this.artwork = StandardArtwork.createLinkedArtworkFromURL(artworkSource.getImageUrl());
        } catch (IOException e) {
            throw PlayqdException.ioException("", e);
        }
    }

    @Override
    public void visit(ArtworkByteArraySource artworkSource) {
        StandardArtwork standardArtwork = new StandardArtwork();

        standardArtwork.setBinaryData(artworkSource.getBinaryData());
        standardArtwork.setMimeType(ImageFormats.getMimeTypeForBinarySignature(artworkSource.getBinaryData()));
        standardArtwork.setDescription("");
        standardArtwork.setPictureType(PictureTypes.DEFAULT_ID);

        this.artwork = standardArtwork;
        visit((AbstractArtworkSource) artworkSource);
    }

    public Artwork getArtwork() {
        return artwork;
    }
}
