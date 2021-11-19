package com.bemonovoid.playsqd.core.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableSearchRequest extends PageableRequest {

    private String search;

    public static PageableSearchRequest empty() {
        var request = new PageableSearchRequest();
        request.setPage(0);
        request.setPageSize(Integer.MAX_VALUE);
        return request;
    }
}
