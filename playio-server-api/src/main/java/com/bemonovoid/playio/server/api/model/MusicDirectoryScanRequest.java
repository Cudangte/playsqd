package com.bemonovoid.playio.server.api.model;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class MusicDirectoryScanRequest {

    @NotEmpty
    private Set<Long> directoryIds;

    private boolean deleteMissing;
    private boolean deleteAllBeforeScan;
}
