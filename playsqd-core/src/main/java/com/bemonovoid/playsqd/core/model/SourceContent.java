package com.bemonovoid.playsqd.core.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SourceContent(
        @JsonProperty("path") String path,
        @JsonProperty("items") List<SourceContentItem> items) {
}
