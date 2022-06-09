package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.dao.AudioSourceDao;
import com.bemonovoid.playsqd.core.dao.AudioTrackDao;
import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.AudioSource;
import com.bemonovoid.playsqd.core.model.AudioSourceWithContent;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.model.SourceContent;
import com.bemonovoid.playsqd.core.model.SourceContentItem;
import com.bemonovoid.playsqd.core.publisher.event.AudioSourceCreatedEvent;
import com.bemonovoid.playsqd.core.service.AudioSourceService;
import com.bemonovoid.playsqd.core.utils.FileUtils;
import com.bemonovoid.playsqd.core.utils.RandomStrings;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
record AudioSourceServiceImpl(ApplicationEventPublisher eventPublisher,
                              AudioSourceDao audioSourceDao,
                              AudioTrackDao audioTrackDao) implements AudioSourceService {

    @Override
    public Collection<AudioSource> getAll() {
        return audioSourceDao.getAll();
    }

    @Override
    public long create(AudioSource source) {
        String path = source.path();

        validatePath(path);

        if (audioSourceDao.existsByPath(path)) {
            throw PlayqdException.objectAlreadyExistsException(
                    String.format("Audio source with the same path: '%s' already exist", path));
        }

        AudioSource audioSource = audioSourceDao.save(source);

        eventPublisher.publishEvent(new AudioSourceCreatedEvent(this, audioSource));

        return audioSource.id();
    }

    @Override
    public void update(AudioSource source) {
        validatePath(source.path());
        audioSourceDao.save(source);
    }

    @Override
    public void delete(long sourceId) {
        audioSourceDao.delete(sourceId);
    }

    @Override
    public AudioSource getById(long sourceId) {
        return audioSourceDao.getById(sourceId);
    }

    @Override
    public AudioSourceWithContent getAudioSourceWithContentForPath(long sourceId, String path) {
        AudioSource source = getById(sourceId);
        Path lookUpPath = Path.of(source.path(), path != null ? path : "");
        try (Stream<Path> files = Files.list(lookUpPath)) {

            Map<Boolean, List<SourceContentItem>> items = files
                    .map(Path::toFile)
                    .filter(f -> f.isDirectory() || (f.isFile() && FileUtils.isSupportedAudioFile(f)))
                    .map(AudioSourceServiceImpl::fromFile)
                    .collect(Collectors.groupingBy(SourceContentItem::isFile));

            List<SourceContentItem> dirItems = items.getOrDefault(Boolean.FALSE, Collections.emptyList());
            List<SourceContentItem> fileItems = items.getOrDefault(Boolean.TRUE, Collections.emptyList());

            List<SourceContentItem> contentItems = new ArrayList<>(dirItems.size() + fileItems.size());

            contentItems.addAll(dirItems);
            contentItems.addAll(fileItems);

            return new AudioSourceWithContent(source, new SourceContent(lookUpPath.toString(), contentItems));
        } catch (IOException e) {
            throw PlayqdException.ioException("Failed to read from path", e);
        }
    }

    @Override
    public List<AudioTrack> audioTracksFromPathInSource(long sourceId, String path) {
        AudioSource source = getById(sourceId);
        Path lookUpPath = Path.of(source.path(), path);
        if (!Files.exists(lookUpPath)) {
            String message = String.format("Path does not exist: %s", lookUpPath);
            throw PlayqdException.ioException(message, 400);
        }
        return audioTrackDao.getAllByFileLocationStartsWith(lookUpPath.toString());
    }

    private static SourceContentItem fromFile(File file) {
        String fileName = file.getName();
        String fileExtension = null;
        String fileSizeHumanReadable = FileUtils.fileDisplaySize(file);
        var isFile = file.isFile();
        if (isFile) {
            var fileNameAndExtension = FileUtils.parseFileNameAndExtension(file.getName());
            fileName = fileNameAndExtension.left();
            fileExtension = fileNameAndExtension.right();
        }
        return new SourceContentItem(
                RandomStrings.randomAlphabetic(8), fileName, isFile, fileExtension, fileSizeHumanReadable);
    }

    private static void validatePath(String path) {
        try {
            if (!Files.exists(Paths.get(path))) {
                throw PlayqdException.ioException(String.format("Audio source for '%s' path does not exist", path), 400);
            }
        } catch (InvalidPathException e) {
            throw PlayqdException.ioException("Audio source path is invalid", 400);
        }
    }
}
