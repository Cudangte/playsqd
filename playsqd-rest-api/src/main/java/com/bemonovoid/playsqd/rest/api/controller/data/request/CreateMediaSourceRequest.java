package com.bemonovoid.playsqd.rest.api.controller.data.request;

import com.bemonovoid.playsqd.core.model.NewMediaSource;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public record CreateMediaSourceRequest(@NotBlank String name,
                                       @NotBlank String path,
                                       @JsonProperty("auto_scan_on_restart") boolean autoScanOnRestart)
        implements NewMediaSource {
}
