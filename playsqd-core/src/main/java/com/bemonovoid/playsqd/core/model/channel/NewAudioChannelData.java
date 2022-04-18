package com.bemonovoid.playsqd.core.model.channel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record NewAudioChannelData(@NotBlank String name,
                                  String description,
                                  @NotNull AudioChannelType type,
                                  @NotBlank String source) {
}