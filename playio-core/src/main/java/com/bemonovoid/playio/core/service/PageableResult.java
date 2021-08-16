package com.bemonovoid.playio.core.service;

import java.util.List;

public interface PageableResult<T> {

    int getTotalPages();

    long getTotalElements();

    int getPage();

    int getPageSize();

    List<T> getData();
}
