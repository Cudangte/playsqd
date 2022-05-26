package com.bemonovoid.playsqd.core.model.channel;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AudioChannel(long id,
                           String name,
                           String description,
                           AudioChannelState state,
                           AudioChannelType type,
                           AudioChannelSelection selection,
                           boolean repeat,
                           List<String> sources) {
}
