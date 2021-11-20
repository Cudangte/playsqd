package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.dao.MediaSourceDao;
import com.bemonovoid.playsqd.core.exception.MediaSourceException;
import com.bemonovoid.playsqd.core.model.MediaSource;
import com.bemonovoid.playsqd.core.model.NewMediaSource;
import com.bemonovoid.playsqd.core.publisher.event.MusicSourceCreatedEvent;
import com.bemonovoid.playsqd.core.publisher.event.MusicSourceScanRequestedEvent;
import com.bemonovoid.playsqd.core.service.MediaSourceService;
import com.bemonovoid.playsqd.core.service.ScannerOptions;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Collection;

@Component
class MediaSourceServiceImpl implements MediaSourceService {

    private final MediaSourceDao mediaSourceDao;
    private final ApplicationEventPublisher eventPublisher;

    MediaSourceServiceImpl(MediaSourceDao mediaSourceDao,
                           ApplicationEventPublisher eventPublisher) {
        this.mediaSourceDao = mediaSourceDao;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void scanSources(Collection<Long> sourceIds, ScannerOptions scannerOptions) {
        Collection<MediaSource> sources = mediaSourceDao.getAllByIds(sourceIds);
        eventPublisher.publishEvent(new MusicSourceScanRequestedEvent(this, sources, scannerOptions));
    }

    @Override
    public long createSource(NewMediaSource newMediaSource) {
        String name = newMediaSource.name();

        if (mediaSourceDao.existsByName(name)) {
            throw MediaSourceException.alreadyExists("name", name);
        }

        String path = newMediaSource.path();

        try {
            Paths.get(path);
        } catch (InvalidPathException e) {
            throw MediaSourceException.ioException(newMediaSource, e);
        }

        if (mediaSourceDao.existsByPath(path)) {
            throw MediaSourceException.alreadyExists("path", path);
        }

        MediaSource mediaSource = mediaSourceDao.create(newMediaSource);

        eventPublisher.publishEvent(new MusicSourceCreatedEvent(this, mediaSource));

        return mediaSource.id();
    }
}
