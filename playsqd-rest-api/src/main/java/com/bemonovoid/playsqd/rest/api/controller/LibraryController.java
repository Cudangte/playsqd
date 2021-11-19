package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.exception.UnsupportedAudioFormatException;
import com.bemonovoid.playsqd.core.model.Album;
import com.bemonovoid.playsqd.core.model.ArtistListItem;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.service.LibraryEditorService;
import com.bemonovoid.playsqd.core.service.LibraryItemFilter;
import com.bemonovoid.playsqd.core.service.LibraryQueryService;
import com.bemonovoid.playsqd.core.service.PageableSearchRequest;
import com.bemonovoid.playsqd.rest.api.model.DataResponse;
import com.bemonovoid.playsqd.rest.api.model.PageableResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/library")
public class LibraryController {

    private final LibraryQueryService libraryQueryService;
    private final LibraryEditorService libraryEditorService;

    public LibraryController(LibraryQueryService libraryQueryService, LibraryEditorService libraryEditorService) {
        this.libraryQueryService = libraryQueryService;
        this.libraryEditorService = libraryEditorService;
    }

    @GetMapping("/artists")
    PageableResponse<ArtistListItem> artists(PageableSearchRequest request) {
        PageableSearchRequest pageableSearchRequest = request != null ? request : PageableSearchRequest.empty();
        return new PageableResponse<>(libraryQueryService.getArtists(pageableSearchRequest));
    }

    @GetMapping("/artists/{artistId}/albums")
    DataResponse<List<Album>> artistAlbums(@PathVariable String artistId) {
        return new DataResponse<>(libraryQueryService.getAlbums(LibraryItemFilter.withId(artistId)).getData());
    }

    @GetMapping(path = "artists/{artistId}/artwork", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    ResponseEntity<byte[]> artistArtwork(@PathVariable String artistId) {
        Optional<byte[]> albumArtworkOpt = libraryQueryService.getArtistArtwork(artistId);
        return albumArtworkOpt
                .map(artwork -> ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)).body(artwork))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("albums/{albumId}/songs")
    DataResponse<Collection<Song>> artistAlbumSongs(@PathVariable String albumId) {
        return new DataResponse<>(libraryQueryService.getArtistAlbumSongs(albumId));
    }

    @GetMapping(path = "albums/{albumId}/artwork", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    ResponseEntity<byte[]> albumArtwork(@PathVariable String albumId) {
        Optional<byte[]> albumArtworkOpt = libraryQueryService.getAlbumArtwork(albumId);
        return albumArtworkOpt
                .map(artwork -> ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)).body(artwork))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/songs/{songId}")
    DataResponse<Song> song(@PathVariable long songId) {
        return new DataResponse<>(libraryQueryService.getSong(songId));
    }

    @PutMapping("/songs/favorite/{songId}")
    void favorite(@PathVariable long songId) {
        libraryEditorService.updateFavoriteStatus(songId);
    }

    @GetMapping(path = "songs/{songId}/artwork", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    ResponseEntity<byte[]> albumArtwork(@PathVariable long songId) {
        Optional<byte[]> albumArtworkOpt = libraryQueryService.getSongArtwork(songId);
        return albumArtworkOpt
                .map(artwork -> ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)).body(artwork))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * // See: Spring's AbstractMessageConverterMethodProcessor implementation that handles byte ranges
     * @param songId
     * @return Audio file stream at the given byte range.
     */
    @GetMapping("/songs/{songId}/stream")
    ResponseEntity<Resource> audioStream(@PathVariable long songId) {

        String fileLocation = libraryQueryService.getSong(songId).getFileLocation();
        String fileType = fileLocation.substring(fileLocation.lastIndexOf(".") + 1);
        if ("mp3".equalsIgnoreCase(fileType)) {
            fileType = "mpeg";
        } else if ("wma".equalsIgnoreCase(fileType)) {
            throw new UnsupportedAudioFormatException("'wma' audio format is not supported");
        } else if ("oga".equalsIgnoreCase(fileType)) {
            fileType = "ogg";
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/" + fileType)
                .body(new FileSystemResource(Paths.get(fileLocation)));
    }
}
