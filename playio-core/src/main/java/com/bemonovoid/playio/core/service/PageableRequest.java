package com.bemonovoid.playio.core.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class PageableRequest {

    private int page;
    private int size;

    public static PageableRequest ofSize(int size) {
        return of(0, size);
    }

    public static PageableRequest of(int page, int size) {
        PageableRequest pageableRequest = new PageableRequest();
        pageableRequest.setPage(page);
        pageableRequest.setSize(size);
        return pageableRequest;
    }
}
