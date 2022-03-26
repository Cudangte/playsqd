package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record AudioSource(Long id,
                          @NotBlank String name,
                          @NotBlank String path,
                          @JsonProperty("auto_scan_on_create") boolean autoScanOnCreate,
                          @JsonProperty("auto_scan_on_restart") boolean autoScanOnRestart,
                          @JsonProperty("delete_missing") boolean deleteMissing,
                          @JsonProperty("delete_all_before_scan")boolean deleteAllBeforeScan,
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME,pattern = "yyyy-MM-dd HH:mm:ss")
                          @JsonProperty("status") AudioSourceScanStatus status,
                          @JsonProperty("status_details") String details,
                          @JsonProperty("last_scan_date") LocalDateTime lastScanDate) {

    @JsonIgnore
    public boolean isNew() {
        return id == null;
    }

}
