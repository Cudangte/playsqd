package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.model.artwork.Artwork;
import com.bemonovoid.playsqd.core.model.artwork.ArtworkByteArraySource;
import com.bemonovoid.playsqd.core.model.artwork.ArtworkSource;
import com.bemonovoid.playsqd.core.model.artwork.ArtworkUrlSource;
import com.bemonovoid.playsqd.core.service.MediaLibraryServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Slf4j
@Validated
@RestController
@RequestMapping(ApiEndpoints.ALBUMS_API_PATH)
class AlbumController {

    private final MediaLibraryServiceContext serviceContext;

    AlbumController(MediaLibraryServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    @GetMapping("/{albumId}/tracks")
    Collection<AudioTrack> tracks(@PathVariable String albumId) {
        return serviceContext.audioTrackQueryService().getAlbumTracks(albumId);
    }

    @GetMapping(path = "/{albumId}/artwork", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    ResponseEntity<byte[]> albumArtwork(@PathVariable String albumId) {
        Artwork artwork = serviceContext.audioTrackQueryService().getAlbumArtwork(albumId);
        if (artwork.isEmpty() || !artwork.hasBinaryData()) {
            log.warn("Artwork for album id: {} was not found", albumId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)).body(artwork.binaryData());
    }

    @PostMapping("/{albumId}/artwork")
    void uploadAlbumArtwork(@PathVariable String albumId,
                            @RequestParam(name = "imageUrl", required = false) String imageUrl,
                            @RequestParam(name = "artwork", required = false) MultipartFile multipart) {
        try {
            ArtworkSource artworkSource = null;
            if (multipart != null && !multipart.isEmpty()) {
                log.info("Uploading album (id: {}) artwork from file", albumId);
                artworkSource = new ArtworkByteArraySource(multipart.getContentType(), multipart.getBytes());
            } else if (imageUrl != null) {
                log.info("Uploading album (id: {}) artwork as image url ", albumId);
                artworkSource = new ArtworkUrlSource(imageUrl);
            } else {
                String message = "Upload failed. Either 'imageUrl' param or 'artwork' multipart must be provided";
                throw PlayqdException.genericException(message, 400);
            }
            serviceContext.audioTrackEditorService().updateAlbumArtwork(albumId, artworkSource);
        } catch (IOException e) {
            throw PlayqdException.ioException("", e);
        }

    }
}
