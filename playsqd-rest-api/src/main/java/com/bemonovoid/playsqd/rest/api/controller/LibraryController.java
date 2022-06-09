package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.model.AlbumInfo;
import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.model.artwork.Artwork;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.service.AlbumSearchCriteria;
import com.bemonovoid.playsqd.core.service.ArtistSearchCriteria;
import com.bemonovoid.playsqd.core.service.AudioTrackQueryService;
import com.bemonovoid.playsqd.core.service.LibraryEditorService;
import com.bemonovoid.playsqd.core.service.LibraryQueryService;
import com.bemonovoid.playsqd.core.service.PageableInfo;
import com.bemonovoid.playsqd.rest.api.controller.data.response.PageableResponse;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.concurrent.TimeUnit;

@Validated
@RestController
@RequestMapping(ApiEndpoints.LIBRARY_API_PATH)
public class LibraryController {

    private final LibraryQueryService libraryQueryService;
    private final AudioTrackQueryService audioTrackQueryService;
    private final LibraryEditorService libraryEditorService;

    public LibraryController(LibraryQueryService libraryQueryService,
                             AudioTrackQueryService audioTrackQueryService,
                             LibraryEditorService libraryEditorService) {
        this.libraryQueryService = libraryQueryService;
        this.libraryEditorService = libraryEditorService;
        this.audioTrackQueryService = audioTrackQueryService;
    }

//    @GetMapping("/artists")
//    PageableResponse<ArtistInfo> artists(@Positive @RequestParam(defaultValue = "1") int page,
//                                         @Positive @RequestParam(name = "page_size", defaultValue = "50") int pageSize,
//                                         @RequestParam(name = "artist_name_like", required = false) String nameLike) {
//        var searchCriteria = new ArtistSearchCriteria(nameLike, new PageableInfo(page, pageSize));
//        return new PageableResponse<>(libraryQueryService.getArtists(searchCriteria));
//    }

    @GetMapping("/albums")
    PageableResponse<AlbumInfo> albums(@Positive @RequestParam(defaultValue = "1") int page,
                                       @Positive @RequestParam(name = "page_size", defaultValue = "50") int pageSize,
                                       @RequestParam(required = false) String name) {
        PageableInfo pageableInfo = new PageableInfo(page, pageSize);
        AlbumSearchCriteria searchCriteria = AlbumSearchCriteria.pageableByName(name, pageableInfo);
        return new PageableResponse<>(libraryQueryService.getAlbums(searchCriteria));
    }

    @GetMapping("/artists/{artistId}/albums")
    PageableResponse<AlbumInfo> artistAlbums(@PathVariable String artistId) {
        return new PageableResponse<>(libraryQueryService.getAlbums(AlbumSearchCriteria.forArtistId(artistId)));
    }

//    @GetMapping(path = "artists/{artistId}/artwork", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
//    ResponseEntity<byte[]> artistArtwork(@PathVariable String artistId) {
//        Optional<byte[]> albumArtworkOpt = libraryQueryService.getArtistArtwork(artistId);
//        return albumArtworkOpt
//                .map(artwork -> ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)).body(artwork))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @GetMapping("albums/{albumId}/songs")
    PageableResponse<AudioTrack> artistAlbumSongs(@PathVariable String albumId) {
        return new PageableResponse<>(libraryQueryService.getArtistAlbumSongs(albumId));
    }

    @GetMapping("/tracks/{trackId}")
    AudioTrack song(@PathVariable long trackId) {
        return libraryQueryService.getAudioTrack(trackId);
    }

    @PutMapping("/songs/favorite/{songId}")
    void favorite(@PathVariable long songId) {
        libraryEditorService.updateFavoriteStatus(songId);
    }

}
