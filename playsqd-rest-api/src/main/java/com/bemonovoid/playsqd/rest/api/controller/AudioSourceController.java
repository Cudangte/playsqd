package com.bemonovoid.playsqd.rest.api.controller;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import com.bemonovoid.playsqd.core.model.AudioSource;
import com.bemonovoid.playsqd.core.model.AudioSourceWithContent;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.service.AudioSourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(ApiEndpoints.SOURCE_API_PATH)
class AudioSourceController {

    private final AudioSourceService audioSourceService;

    AudioSourceController(AudioSourceService audioSourceService) {
        this.audioSourceService = audioSourceService;
    }

    @PostMapping
    ResponseEntity<Object> create(@RequestBody @Valid AudioSource source) {
        long createdSourceId = audioSourceService.create(source);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{sourceId}")
                .buildAndExpand(createdSourceId)
                .toUri();
        return ResponseEntity.created(location).body(createdSourceId);
    }

    @GetMapping("/{sourceId}")
    AudioSource get(@PathVariable long sourceId) {
        return audioSourceService.getById(sourceId);
    }

    @GetMapping("/{sourceId}/content")
    AudioSourceWithContent content(@PathVariable long sourceId,
                                   @RequestParam(name = "path", required = false) String path) {
       return audioSourceService.getAudioSourceWithContentForPath(sourceId, path);
    }

    @GetMapping("/{sourceId}/content/tracks")
    List<AudioTrack> audioTracksFromPathInSource(@PathVariable long sourceId,
                                  @RequestParam(name = "path") String path) {
        return audioSourceService.audioTracksFromPathInSource(sourceId, path);
    }

    @PatchMapping
    void update(@RequestBody @Valid AudioSource source) {
        audioSourceService.update(source);
    }

    @DeleteMapping("/{sourceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable long sourceId) {
        audioSourceService.delete(sourceId);
    }

    @GetMapping
    Collection<AudioSource> getAll() {
        return audioSourceService.getAll();
    }

}
