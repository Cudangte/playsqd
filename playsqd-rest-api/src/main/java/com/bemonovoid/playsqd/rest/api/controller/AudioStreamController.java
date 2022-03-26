package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.service.LibraryQueryService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;

@RestController
@RequestMapping(ApiEndpoints.STREAM_API_PATH)
class AudioStreamController {

    private final LibraryQueryService libraryQueryService;

    AudioStreamController(LibraryQueryService libraryQueryService) {
        this.libraryQueryService = libraryQueryService;
    }

    /**
     * // See: Spring's AbstractMessageConverterMethodProcessor implementation that handles byte ranges
     * @param songId
     * @return Audio file stream at the given byte range.
     */
    @GetMapping("/songs/{songId}/stream")
    ResponseEntity<Resource> audioStream(@PathVariable long songId) {

        String fileLocation = libraryQueryService.getSong(songId).getFileLocation();
        String fileType = fileLocation.substring(fileLocation.lastIndexOf(".") + 1);
        if ("mp3".equalsIgnoreCase(fileType)) {
            fileType = "mpeg";
        } else if ("wma".equalsIgnoreCase(fileType)) {
            throw PlayqdException.ioException("'wma' audio format is not supported", 400);
        } else if ("oga".equalsIgnoreCase(fileType)) {
            fileType = "ogg";
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/" + fileType)
                .body(new FileSystemResource(Paths.get(fileLocation)));
    }
}
