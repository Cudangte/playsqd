package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.dao.MediaDirectoryDao;
import com.bemonovoid.playsqd.core.model.MediaDirectory;
import com.bemonovoid.playsqd.core.publisher.event.MusicDirectoryAddedEvent;
import com.bemonovoid.playsqd.core.service.MediaDirectoryScanner;
import com.bemonovoid.playsqd.core.service.ScanOptions;
import com.bemonovoid.playsqd.rest.api.model.MusicDirectoryJsonModel;
import com.bemonovoid.playsqd.rest.api.model.MusicDirectoryScanRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/library/directory")
class MusicDirectoryController {

    private final ApplicationEventPublisher eventPublisher;
    private final MediaDirectoryDao mediaDirectoryDao;
    private final MediaDirectoryScanner mediaDirectoryScanner;

    MusicDirectoryController(ApplicationEventPublisher eventPublisher,
                             MediaDirectoryDao mediaDirectoryDao,
                             MediaDirectoryScanner mediaDirectoryScanner) {
        this.eventPublisher = eventPublisher;
        this.mediaDirectoryDao = mediaDirectoryDao;
        this.mediaDirectoryScanner = mediaDirectoryScanner;
    }

    @PostMapping
    ResponseEntity<Object> addDirectory(@RequestBody @Valid List<MusicDirectoryJsonModel> models) {
        try {
            Collection<Long> addedDirectoryIds = new ArrayList<>(models.size());
            for (MusicDirectoryJsonModel model : models) {
                long id = mediaDirectoryDao.add(MediaDirectory.builder()
                        .type(model.getType())
                        .path(Paths.get(model.getPath()))
                        .build());
                addedDirectoryIds.add(id);
                boolean autoScan = Optional.ofNullable(model.getAutoScan()).orElse(false);
                eventPublisher.publishEvent(new MusicDirectoryAddedEvent(this, id, autoScan));
            }

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
            return ResponseEntity.created(location).body(addedDirectoryIds);
        } catch (InvalidPathException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{directoryId}")
    MusicDirectoryJsonModel getMusicDirectory(@PathVariable long directoryId) {
        return buildModel(mediaDirectoryDao.getMediaDirectory(directoryId));
    }

    @GetMapping
    List<MusicDirectoryJsonModel> getAll() {
        return mediaDirectoryDao.getAll().stream().map(this::buildModel).collect(Collectors.toList());
    }

    @PostMapping("/scanner")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void scan(@RequestBody @Valid MusicDirectoryScanRequest request) {
        var scanOptions =
                new ScanOptions(request.isDeleteMissing(), request.isDeleteAllBeforeScan(), request.getDirectoryIds());
        mediaDirectoryScanner.scan(scanOptions);
    }

    private MusicDirectoryJsonModel buildModel(MediaDirectory mediaDirectory) {
        return MusicDirectoryJsonModel.builder()
                .id(mediaDirectory.getId())
                .type(mediaDirectory.getType())
                .path(mediaDirectory.getPath().toString())
                .build();
    }

}
