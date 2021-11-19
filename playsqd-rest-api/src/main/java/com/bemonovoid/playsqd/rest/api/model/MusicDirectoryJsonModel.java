package com.bemonovoid.playsqd.rest.api.model;

import com.bemonovoid.playsqd.core.model.MusicDirectoryType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MusicDirectoryJsonModel {

    private long id;

    @NotNull
    private MusicDirectoryType type;

    @NotNull
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean autoScan;

}
