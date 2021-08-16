package com.bemonovoid.playio.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.nio.file.Path;

@Getter
@Builder
@ToString
public class MusicDirectory {

    private final long id;
    private final Path path;
    private final MusicDirectoryType type;
}
