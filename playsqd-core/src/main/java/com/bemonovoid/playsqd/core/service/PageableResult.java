package com.bemonovoid.playsqd.core.service;

import java.util.List;

public interface PageableResult<T> {

    int getTotalPages();

    long getTotalElements();

    int getPage();

    int getPageSize();

    List<T> getData();

    default boolean isEmpty() {
        return getData().isEmpty();
    }
}
