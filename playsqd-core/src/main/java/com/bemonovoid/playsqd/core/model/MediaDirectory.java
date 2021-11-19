package com.bemonovoid.playsqd.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.nio.file.Path;

@Getter
@Builder
@ToString
public class MediaDirectory {

    private final long id;
    private final Path path;
    private final MusicDirectoryType type;
}
