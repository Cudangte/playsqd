package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.model.Genre;
import com.bemonovoid.playsqd.core.service.LibraryQueryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Validated
@RestController
@RequestMapping(ApiEndpoints.GENRES_API_PATH)
class GenreController {

    private final LibraryQueryService libraryQueryService;

    GenreController(LibraryQueryService libraryQueryService) {
        this.libraryQueryService = libraryQueryService;
    }

    @GetMapping
    Collection<Genre> genres() {
        return libraryQueryService.getGenres();
    }
}
