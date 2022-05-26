package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SourceContentItem(
        @JsonProperty("name") String fileName,
        @JsonProperty("extension") String fileExtension,
        @JsonProperty("is_file") boolean isFile) {
}
