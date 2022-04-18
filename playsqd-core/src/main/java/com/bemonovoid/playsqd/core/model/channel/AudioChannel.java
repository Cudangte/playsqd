package com.bemonovoid.playsqd.core.model.channel;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AudioChannel(long id,
                           String name,
                           String description,
                           AudioChannelState state,
                           AudioChannelType type,
                           AudioChannelSelection selection,
                           String source) {
}
