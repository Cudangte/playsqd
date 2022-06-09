package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.model.ArtistInfo;
import com.bemonovoid.playsqd.core.service.ArtistSearchCriteria;
import com.bemonovoid.playsqd.core.service.AudioTrackQueryService;
import com.bemonovoid.playsqd.core.service.PageableInfo;
import com.bemonovoid.playsqd.rest.api.controller.data.response.PageableResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping(ApiEndpoints.ARTISTS_API_PATH)
class ArtistController {

    private final AudioTrackQueryService audioTrackQueryService;

    ArtistController(AudioTrackQueryService audioTrackQueryService) {
        this.audioTrackQueryService = audioTrackQueryService;
    }

    @GetMapping
    PageableResponse<ArtistInfo> artists(@Positive @RequestParam(defaultValue = "1") int page,
                                         @Positive @RequestParam(name = "page_size", defaultValue = "50") int pageSize,
                                         @RequestParam(name = "name_like", required = false) String nameLike,
                                         @RequestParam(name = "name_starts_with", required = false) String nameStartsWith) {
        var searchCriteria = new ArtistSearchCriteria(nameLike, nameStartsWith, new PageableInfo(page, pageSize));
        return new PageableResponse<>(audioTrackQueryService.getArtists(searchCriteria));
    }
}
