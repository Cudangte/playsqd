package com.bemonovoid.playsqd.core.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LibraryItemFilter {

    private String id;
    private String name;
    private PageableInfo pageable;

    public static LibraryItemFilter withId(String id) {
        return LibraryItemFilter.builder().id(id).build();
    }
}
