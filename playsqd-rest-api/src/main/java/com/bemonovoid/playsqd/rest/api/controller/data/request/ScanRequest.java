package com.bemonovoid.playsqd.rest.api.controller.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public record ScanRequest(
        @NotEmpty @JsonProperty("source_ids") Set<Long> sourceIds,
        @JsonProperty("delete_missing") boolean deleteMissing,
        @JsonProperty("delete_all_before_scan") boolean deleteAllBeforeScan) {

}
