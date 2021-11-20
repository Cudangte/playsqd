package com.bemonovoid.playsqd.rest.api.controller.data.response;

import com.bemonovoid.playsqd.core.service.PageableResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"data", "pagination"})
public class PageableResponse<T> {

    @JsonIgnore
    private final PageableResult<T> result;

    public PageableResponse(PageableResult<T> result) {
        this.result = result;
    }

    @JsonProperty("pagination")
    public final Pagination getPagination() {
        return new Pagination(
                result.getPage() == 0 ? 1 : result.getPage(),
                result.getPageSize(),
                result.getTotalPages(),
                result.getTotalElements());
    }

    @JsonProperty("data")
    public final List<T> getData() {
        return result.getData();
    }

    public static record Pagination(int page,
                                    @JsonProperty("page_size") int pageSize,
                                    @JsonProperty("total_pages") int totalPages,
                                    @JsonProperty("total_elements") long totalElements) {
    }
}
