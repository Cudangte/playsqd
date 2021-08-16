package com.bemonovoid.playio.core.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LibraryItemFilter {

    private String id;
    private String name;
    private PageableRequest pageable;

    public static LibraryItemFilter withId(String id) {
        return LibraryItemFilter.builder().id(id).build();
    }
}
