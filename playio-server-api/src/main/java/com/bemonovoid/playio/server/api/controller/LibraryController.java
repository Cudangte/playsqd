package com.bemonovoid.playio.server.api.controller;

import java.util.List;

import com.bemonovoid.playio.core.model.Album;
import com.bemonovoid.playio.core.model.Artist;
import com.bemonovoid.playio.core.model.ArtistAlbumSong;
import com.bemonovoid.playio.core.model.ArtistAlbumSongs;
import com.bemonovoid.playio.core.service.LibraryItemFilter;
import com.bemonovoid.playio.core.service.LibraryQueryService;
import com.bemonovoid.playio.core.service.PageableRequest;
import com.bemonovoid.playio.server.api.model.DataResponse;
import com.bemonovoid.playio.server.api.model.PageableResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library")
public class LibraryController {

    private final LibraryQueryService libraryQueryService;

    public LibraryController(LibraryQueryService libraryQueryService) {
        this.libraryQueryService = libraryQueryService;
    }

    @GetMapping("/artists")
    PageableResponse<Artist> artists(@RequestParam(required = false) PageableRequest request) {
        PageableRequest pageableRequest = request != null ? request : PageableRequest.ofSize(50);
        return new PageableResponse<>(libraryQueryService.getArtists(pageableRequest));
    }

    @GetMapping("/artists/{artistId}/albums")
    DataResponse<List<Album>> artistAlbums(@PathVariable String artistId) {
        return new DataResponse<>(libraryQueryService.getAlbums(LibraryItemFilter.withId(artistId)).getData());
    }

    @GetMapping("albums/{albumId}/songs")
    DataResponse<ArtistAlbumSongs> artistAlbumSongs(@PathVariable String albumId) {
        return new DataResponse<>(libraryQueryService.getArtistAlbumSongs(albumId));
    }

    @GetMapping("/songs/{songId}")
    DataResponse<ArtistAlbumSong> song(@PathVariable long songId) {
        return new DataResponse<>(libraryQueryService.getSong(songId));
    }
}
