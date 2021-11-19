package com.bemonovoid.playsqd.persistence.jdbc.dao;

import java.util.List;

import com.bemonovoid.playsqd.core.service.PageableResult;
import org.springframework.data.domain.Page;

class PageableResultImpl<T> implements PageableResult<T> {

    private final Page<T> page;

    PageableResultImpl(Page<T> page) {
        this.page = page;
    }

    @Override
    public int getTotalPages() {
        return page.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return page.getTotalElements();
    }

    @Override
    public int getPage() {
        return page.getNumber();
    }

    @Override
    public int getPageSize() {
        return page.getSize();
    }

    @Override
    public List<T> getData() {
        return page.getContent();
    }

    public static <T> PageableResult<T> empty() {
        return new PageableResultImpl<>(Page.empty());
    }
}
