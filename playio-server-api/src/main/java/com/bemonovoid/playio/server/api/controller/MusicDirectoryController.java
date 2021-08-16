package com.bemonovoid.playio.server.api.controller;

import com.bemonovoid.playio.core.dao.MusicDirectoryDao;
import com.bemonovoid.playio.core.model.MusicDirectory;
import com.bemonovoid.playio.core.publisher.event.MusicDirectoryAddedEvent;
import com.bemonovoid.playio.core.service.MusicDirectoryScanner;
import com.bemonovoid.playio.core.service.ScanOptions;
import com.bemonovoid.playio.server.api.model.MusicDirectoryJsonModel;
import com.bemonovoid.playio.server.api.model.MusicDirectoryScanRequest;
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
    private final MusicDirectoryDao musicDirectoryDao;
    private final MusicDirectoryScanner musicDirectoryScanner;

    MusicDirectoryController(ApplicationEventPublisher eventPublisher,
                             MusicDirectoryDao musicDirectoryDao,
                             MusicDirectoryScanner musicDirectoryScanner) {
        this.eventPublisher = eventPublisher;
        this.musicDirectoryDao = musicDirectoryDao;
        this.musicDirectoryScanner = musicDirectoryScanner;
    }

    @PostMapping
    ResponseEntity<Object> addDirectory(@RequestBody @Valid List<MusicDirectoryJsonModel> models) {
        try {
            Collection<Long> addedDirectoryIds = new ArrayList<>(models.size());
            for (MusicDirectoryJsonModel model : models) {
                long id = musicDirectoryDao.add(MusicDirectory.builder()
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
        return buildModel(musicDirectoryDao.getMusicDirectory(directoryId));
    }

    @GetMapping
    List<MusicDirectoryJsonModel> getAll() {
        return musicDirectoryDao.getAll().stream().map(this::buildModel).collect(Collectors.toList());
    }

    @PostMapping("/scanner")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void scan(@RequestBody @Valid MusicDirectoryScanRequest request) {
        var scanOptions = ScanOptions.builder()
                .deleteMissing(request.isDeleteMissing())
                .deleteAllBeforeScan(request.isDeleteAllBeforeScan())
                .directoryIds(request.getDirectoryIds())
                .build();
        musicDirectoryScanner.scan(scanOptions);
    }

    private MusicDirectoryJsonModel buildModel(MusicDirectory musicDirectory) {
        return MusicDirectoryJsonModel.builder()
                .id(musicDirectory.getId())
                .type(musicDirectory.getType())
                .path(musicDirectory.getPath().toString())
                .build();
    }

}
