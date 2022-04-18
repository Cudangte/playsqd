package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.config.properties.AudioChannelConfiguration;
import com.bemonovoid.playsqd.core.dao.AudioChannelDao;
import com.bemonovoid.playsqd.core.dao.MediaLibraryDao;
import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.model.channel.AudioChannel;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelPlaybackItem;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelState;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelWithStreamingItemInfo;
import com.bemonovoid.playsqd.core.model.channel.NewAudioChannelData;
import com.bemonovoid.playsqd.core.publisher.event.AudioChannelItemStreamedEvent;
import com.bemonovoid.playsqd.core.service.AudioChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
record AudioChannelServiceImpl(ApplicationEventPublisher publisher,
                               AudioChannelConfiguration audioChannelConfiguration,
                               AudioChannelDao audioChannelDao,
                               MediaLibraryDao mediaLibraryDao) implements AudioChannelService {

    @Override
    public AudioChannel create(NewAudioChannelData channelData) {
        AudioChannel audioChannel = audioChannelDao.createChannel(channelData);
        if (!mediaLibraryDao.existsByAlbumGenreLikeIgnoreCase(audioChannel.source())) {
            audioChannelDao.modifyChannelState(audioChannel.id(), AudioChannelState.EMPTY);
        }
        log.info("Audio channel with id: '{}' has successfully been created", audioChannel.id());
        return audioChannel;
    }

    @Override
    public AudioChannel getChannel(long channelId) {
        return audioChannelDao.findChannelByIdOrThrow(channelId);
    }

    @Override
    public Collection<AudioChannelPlaybackItem> getChannelPlaybackSongs(long channelId) {
        return audioChannelDao.finaAllChannelPlaybackSongs(channelId);
    }

    @Override
    public void deleteChannelPlaybackHistory(long channelId) {
        audioChannelDao.deletePlaybackHistory(channelId);
    }

    @Override
    public synchronized AudioChannelWithStreamingItemInfo audioChannelWithStreamingItemInfo(long channelId) {
        var mayBeChannelStreamInfo = audioChannelDao.findChannelStreamInfoByChannelId(channelId);

        if (mayBeChannelStreamInfo.isEmpty()) {
            var song = getNextChannelSongToPlay(channelId, getChannel(channelId).source());
            var channelStreamInfo = audioChannelDao.createChannelStreamInfo(channelId, song);

            publishChannelItemStreamedEvent(channelId, song.getId());

            return new AudioChannelWithStreamingItemInfo(channelId, song, channelStreamInfo.streamingTimestamp());
        } else {
            var steamInfo = mayBeChannelStreamInfo.get();
            var streamingItemCalculatedEndDate =
                    steamInfo.streamingTimestamp().plusSeconds(steamInfo.streamingItemLengthInSec());

            if (streamingItemCalculatedEndDate.isBefore(LocalDateTime.now())) {
                log.info("Steaming item with id: '{}' may have reached the end, searching for next item to stream ...",
                        steamInfo.streamingItemId());

                var song = getNextChannelSongToPlay(channelId, getChannel(channelId).source());

                log.info("Next streaming item has successfully resolved to item with id: {}", song.getId());

                var channelStreamInfo = audioChannelDao.updateChannelStreamInfo(channelId, song);

                publishChannelItemStreamedEvent(channelId, song.getId());

                return new AudioChannelWithStreamingItemInfo(channelId, song, channelStreamInfo.streamingTimestamp());
            }

            var song = mediaLibraryDao.getSong(steamInfo.streamingItemId());

            log.info("Current streaming item has not reached the end yet. Continue streaming the same item with id: {}",
                    song.getId());

            return new AudioChannelWithStreamingItemInfo(channelId, song, steamInfo.streamingTimestamp());
        }
    }

    @Override
    public List<AudioChannel> getAllChannels() {
        return audioChannelDao.findAllChannels();
    }

    private void publishChannelItemStreamedEvent(long channelId, long streamedItemId) {
        publisher.publishEvent(new AudioChannelItemStreamedEvent(this, channelId, streamedItemId));
    }

    private Song getNextChannelSongToPlay(long channelId, String genre) {
        Optional<Song> nextSong = findRandomGenreSongNotYetStreamedByChannelId(channelId, genre);
        if (nextSong.isEmpty() && audioChannelConfiguration.isRepeatAll()) {
            audioChannelDao.deletePlaybackHistory(channelId);
            nextSong = findRandomGenreSongNotYetStreamedByChannelId(channelId, genre);
        }
        return nextSong.orElseThrow(() -> PlayqdException.objectDoesNotExistException(genre, "GenreSong"));
    }

    private Optional<Song> findRandomGenreSongNotYetStreamedByChannelId(long channelId, String genre) {
        return mediaLibraryDao.findRandomGenreSongNotYetStreamedByChannelId(channelId, genre);
    }
}
