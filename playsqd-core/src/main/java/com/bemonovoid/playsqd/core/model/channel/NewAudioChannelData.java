package com.bemonovoid.playsqd.core.model.channel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public record NewAudioChannelData(@NotBlank String name,
                                  String description,
                                  @NotNull AudioChannelType type,
                                  @NotEmpty List<String> sources,
                                  boolean repeat) {
}