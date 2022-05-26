package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.service.LibraryQueryService;
import com.bemonovoid.playsqd.core.utils.AudioFileExtension;
import com.bemonovoid.playsqd.core.utils.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import java.nio.file.Paths;

@RestController
@RequestMapping(ApiEndpoints.STREAM_API_PATH)
class AudioStreamController {

    private final LibraryQueryService libraryQueryService;

    AudioStreamController(LibraryQueryService libraryQueryService) {
        this.libraryQueryService = libraryQueryService;
    }

    /**
     * See: Spring's {@link AbstractMessageConverterMethodProcessor} implementation that handles byte ranges
     * @param trackId
     * @return Audio file stream at the given byte range.
     */
    @GetMapping("/{trackId}")
    ResponseEntity<Resource> audioTrackStream(@PathVariable long trackId) {

        String fileLocation = libraryQueryService.getAudioTrack(trackId).fileLocation();
        String fileExtension = FileUtils.getFileExtension(fileLocation);
        if (AudioFileExtension.MP3.valueEqualsIgnoreCase(fileExtension)) {
            fileExtension = AudioFileExtension.MPEG.getValue();
        } else if (AudioFileExtension.WMA.valueEqualsIgnoreCase(fileExtension)) {
            throw PlayqdException.ioException("'wma' audio format is not supported", 400);
        } else if (AudioFileExtension.OGA.valueEqualsIgnoreCase(fileExtension)) {
            fileExtension = AudioFileExtension.OGG.getValue();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/" + fileExtension)
                .body(new FileSystemResource(Paths.get(fileLocation)));
    }

}
