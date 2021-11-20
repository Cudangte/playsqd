package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.service.MediaSourceService;
import com.bemonovoid.playsqd.core.service.ScannerOptions;
import com.bemonovoid.playsqd.rest.api.controller.data.request.CreateMediaSourceRequest;
import com.bemonovoid.playsqd.rest.api.controller.data.request.ScanRequest;
import com.bemonovoid.playsqd.rest.api.model.MusicSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.SETTINGS_API_PATH + "/sources")
class MediaSourcesController {

    private final MediaSourceService mediaSourceService;

    MediaSourcesController(MediaSourceService mediaSourceService) {
        this.mediaSourceService = mediaSourceService;
    }

    @PostMapping
    ResponseEntity<Object> createSource(@RequestBody @Valid CreateMediaSourceRequest request) {
        long createdSourceId = mediaSourceService.createSource(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{sourceId}")
                .buildAndExpand(createdSourceId)
                .toUri();
        return ResponseEntity.created(location).body(createdSourceId);
    }

    @GetMapping("/{sourceId}")
    MusicSource getMusicDirectory(@PathVariable long sourceId) {
        return null;
    }

    @GetMapping
    List<MusicSource> getAll() {
        return null;
//        return mediaSourceDao.getAll().stream().map(this::buildModel).collect(Collectors.toList());
    }

    @PostMapping("/scan")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void scan(@RequestBody @Valid ScanRequest request) {
        var scanOptions = new ScannerOptions(request.deleteMissing(), request.deleteAllBeforeScan());
        mediaSourceService.scanSources(request.sourceIds(), scanOptions);
    }

}
