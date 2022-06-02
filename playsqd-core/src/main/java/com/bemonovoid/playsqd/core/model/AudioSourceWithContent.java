package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AudioSourceWithContent(
        @JsonProperty("source") AudioSource audioSource,
        @JsonProperty("content") SourceContent audioSourceContent) {
}
