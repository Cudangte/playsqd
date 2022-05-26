package com.bemonovoid.playsqd.rest.api.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(ApiEndpoints.ARTISTS_API_PATH)
class ArtistController {
}
