package com.bemonovoid.playio.server.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataResponse<T> {

    private final T data;
}
