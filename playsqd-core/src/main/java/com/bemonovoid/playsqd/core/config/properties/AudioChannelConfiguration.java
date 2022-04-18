package com.bemonovoid.playsqd.core.config.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter(AccessLevel.PACKAGE)
public class AudioChannelConfiguration {

    private boolean repeatAll;
}
