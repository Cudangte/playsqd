package com.bemonovoid.playsqd.core.service;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class PageableRequest {

    @PositiveOrZero
    private int page;

    private int pageSize;

    public static PageableRequest ofSize(int pageSize) {
        return of(0, pageSize);
    }

    public static PageableRequest of(int page, int pageSize) {
        PageableRequest pageableRequest = new PageableRequest();
        pageableRequest.setPage(page);
        pageableRequest.setPageSize(pageSize);
        return pageableRequest;
    }
}
