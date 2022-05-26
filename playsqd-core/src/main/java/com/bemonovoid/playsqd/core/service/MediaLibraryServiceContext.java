package com.bemonovoid.playsqd.core.service;

import org.springframework.stereotype.Component;

@Component
public record MediaLibraryServiceContext(AudioTrackQueryService audioTrackQueryService,
                                         AudioTrackEditorService audioTrackEditorService) {
}
