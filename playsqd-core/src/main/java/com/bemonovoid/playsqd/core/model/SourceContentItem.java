package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SourceContentItem(
        @JsonProperty("name") String name,
        @JsonProperty("is_directory") boolean isDirectory,
        @JsonProperty("is_file") boolean isFile,
        @JsonProperty("extension") String fileExtension) {
}
