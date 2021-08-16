package com.bemonovoid.playio.server.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.bemonovoid.playio.core.service.PageableResult;
import lombok.Getter;

@JsonPropertyOrder({"totalElements", "totalPages", "page", "pageSize", "data"})
@Getter
public class PageableResponse<T> {

    @JsonIgnore
    private final PageableResult<T> result;

    public PageableResponse(PageableResult<T> result) {
        this.result = result;
    }

    @JsonProperty(value = "totalPages")
    public int getTotalPages() {
        return result.getTotalPages();
    }

    @JsonProperty("totalElements")
    public long totalElements() {
        return result.getTotalElements();
    }

    @JsonProperty("page")
    public int getPage() {
        return result.getPage();
    }

    @JsonProperty("pageSize")
    public int getPageSize() {
        return result.getPageSize();
    }

    public final List<T> getData() {
        return result.getData();
    }
}
