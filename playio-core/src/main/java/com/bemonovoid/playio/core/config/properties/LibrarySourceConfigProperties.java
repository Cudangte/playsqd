package com.bemonovoid.playio.core.config.properties;

import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class LibrarySourceConfigProperties {

    private Set<String> local;
}
