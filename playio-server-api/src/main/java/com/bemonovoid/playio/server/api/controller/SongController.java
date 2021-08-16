package com.bemonovoid.playio.server.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/library/songs")
class SongController {

    @GetMapping
    List<String> songs() {
        return List.of("1", "2");
    }
}
