package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.service.AudioSourceScanner;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping(ApiEndpoints.SETTINGS_API_PATH + "/sources")
class AudioSourceScannerController {

    private final AudioSourceScanner audioSourceScanner;

    AudioSourceScannerController(AudioSourceScanner audioSourceScanner) {
        this.audioSourceScanner = audioSourceScanner;
    }

    @PostMapping("/scan/{sourceId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void scan(@PathVariable @Positive long sourceId) {
        audioSourceScanner.scan(sourceId);
    }
}
