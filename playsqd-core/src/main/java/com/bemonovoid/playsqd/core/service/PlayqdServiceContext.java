package com.bemonovoid.playsqd.core.service;

import org.springframework.stereotype.Component;

@Component
public record PlayqdServiceContext(AudioSourceService audioSourceService,
                                   AudioChannelService audioChannelService) {
}
