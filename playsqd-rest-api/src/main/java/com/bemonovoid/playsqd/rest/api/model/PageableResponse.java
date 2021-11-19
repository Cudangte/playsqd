package com.bemonovoid.playsqd.rest.api.model;

import com.bemonovoid.playsqd.core.service.PageableResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@JsonPropertyOrder({"data", "pagination"})
public class PageableResponse<T> {

    @JsonIgnore
    private final PageableResult<T> result;

    public PageableResponse(PageableResult<T> result) {
        this.result = result;
    }

    public final Pagination getPagination(){
        return new Pagination(result.getPage(), result.getPageSize(), result.getTotalPages(), result.getTotalElements());
    }

    public final List<T> getData() {
        return result.getData();
    }

    @Getter
    @AllArgsConstructor
    public static class Pagination {
        private int page;
        private int pageSize;
        private int totalPages;
        private long totalElements;
    }
}
