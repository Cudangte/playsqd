package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.exception.UnsupportedAudioFormatException;
import com.bemonovoid.playsqd.core.model.Album;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.service.*;
import com.bemonovoid.playsqd.rest.api.controller.data.response.PageableResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(ApiEndpoints.LIBRARY_API_PATH)
public class LibraryController {

    private final LibraryQueryService libraryQueryService;
    private final LibraryEditorService libraryEditorService;

    public LibraryController(LibraryQueryService libraryQueryService, LibraryEditorService libraryEditorService) {
        this.libraryQueryService = libraryQueryService;
        this.libraryEditorService = libraryEditorService;
    }

    @GetMapping("/artists")
    PageableResponse<ArtistInfo> artists(@Positive @RequestParam(defaultValue = "1") int page,
                                         @Positive @RequestParam(name = "page_size", defaultValue = "50") int pageSize,
                                         @RequestParam(required = false) String search) {
        var pageableSearchRequest = new PageableSearch(search, new PageableInfo(page, pageSize));
        return new PageableResponse<>(libraryQueryService.getArtists(pageableSearchRequest));
    }

    @GetMapping("/artists/{artistId}/albums")
    PageableResponse<Album> artistAlbums(@PathVariable String artistId) {
        return new PageableResponse<>(libraryQueryService.getAlbums(LibraryItemFilter.withId(artistId)));
    }

    @GetMapping(path = "artists/{artistId}/artwork", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    ResponseEntity<byte[]> artistArtwork(@PathVariable String artistId) {
        Optional<byte[]> albumArtworkOpt = libraryQueryService.getArtistArtwork(artistId);
        return albumArtworkOpt
                .map(artwork -> ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)).body(artwork))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("albums/{albumId}/songs")
    PageableResponse<Song> artistAlbumSongs(@PathVariable String albumId) {
        return new PageableResponse<>(libraryQueryService.getArtistAlbumSongs(albumId));
    }

    @GetMapping(path = "albums/{albumId}/artwork", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    ResponseEntity<byte[]> albumArtwork(@PathVariable String albumId) {
        Optional<byte[]> albumArtworkOpt = libraryQueryService.getAlbumArtwork(albumId);
        return albumArtworkOpt
                .map(artwork -> ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)).body(artwork))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/songs/{songId}")
    Song song(@PathVariable long songId) {
        return libraryQueryService.getSong(songId);
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
