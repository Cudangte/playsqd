package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SourceContentItem(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("is_file") boolean isFile,
        @JsonProperty("extension") String fileExtension,
        @JsonProperty("file_size") String fileSize) {
}
