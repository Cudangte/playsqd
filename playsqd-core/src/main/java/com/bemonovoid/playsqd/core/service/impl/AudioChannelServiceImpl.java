package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.config.properties.AudioChannelConfiguration;
import com.bemonovoid.playsqd.core.dao.AudioChannelDao;
import com.bemonovoid.playsqd.core.dao.AudioTrackDao;
import com.bemonovoid.playsqd.core.exception.PlayqdException;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.model.channel.AudioChannel;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelWithPlaybackInfo;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelPlayedTrack;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelState;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelTrack;
import com.bemonovoid.playsqd.core.model.channel.NewAudioChannelData;
import com.bemonovoid.playsqd.core.publisher.event.AudioChannelTrackIsNowPlayingEvent;
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
                               AudioTrackDao audioTrackDao) implements AudioChannelService {

    @Override
    public AudioChannel create(NewAudioChannelData channelData) {
        AudioChannel audioChannel = audioChannelDao.createChannel(channelData);
        if (!audioTrackDao.existsByAlbumGenreInIgnoreCase(audioChannel.sources())) {
            audioChannelDao.updateState(audioChannel.id(), AudioChannelState.EMPTY);
        }
        log.info("Audio channel with id: '{}' has successfully been created", audioChannel.id());
        return audioChannel;
    }

    @Override
    public AudioChannel getChannel(long channelId) {
        return audioChannelDao.findChannelByIdOrThrow(channelId);
    }

    @Override
    public AudioChannelWithPlaybackInfo getChannelExtended(long channelId) {
        AudioChannel channel = getChannel(channelId);
        return new AudioChannelWithPlaybackInfo(
                channel,
                getChannelPlayedTracks(channelId),
                audioTrackDao().getCountByGenres(channel.sources()));
    }

    @Override
    public Collection<AudioChannelPlayedTrack> getChannelPlayedTracks(long channelId) {
        return audioChannelDao.findPlayedTracks(channelId);
    }

    @Override
    public void deleteChannelPlaybackHistory(long channelId) {
        audioChannelDao.deletePlayedTracksByChannelId(channelId);
    }

    @Override
    public synchronized AudioChannelTrack audioChannelNowPlayingTrack(long channelId) {
        var mayBeNowPLayingTrack = audioChannelDao.findNowPLayingTrackByChannelId(channelId);

        if (mayBeNowPLayingTrack.isEmpty()) {
            var audioTrack = selectNextTrackToPlay(getChannel(channelId));
            var nowPlayingTrack = audioChannelDao.createNowPlayingTrack(channelId, audioTrack);

            publishAudioTrackIsNowPlayingEvent(channelId, audioTrack.id());

            return new AudioChannelTrack(channelId, audioTrack, nowPlayingTrack.playStartDate());
        } else {
            var nowPlayingTrack = mayBeNowPLayingTrack.get();
            var calculatedEndDate =
                    nowPlayingTrack.playStartDate().plusSeconds(nowPlayingTrack.audioTrackItemLengthInSec());

            if (calculatedEndDate.isBefore(LocalDateTime.now())) {
                log.info("Steaming item with id: '{}' may have reached the end, searching for next item to stream ...",
                        nowPlayingTrack.audioTrackId());

                var audioTrack = selectNextTrackToPlay(getChannel(channelId));

                log.info("Next streaming item has successfully resolved to item with id: {}", audioTrack.id());

                var nextNowPlayingTrack = audioChannelDao.updateNowPlayingTrack(channelId, audioTrack);

                publishAudioTrackIsNowPlayingEvent(channelId, audioTrack.id());

                return new AudioChannelTrack(channelId, audioTrack, nextNowPlayingTrack.playStartDate());
            }

            var song = audioTrackDao.getTrackById(nowPlayingTrack.audioTrackId());

            log.info("Current streaming item has not reached the end yet. Continue streaming the same item with id: {}",
                    song.id());

            return new AudioChannelTrack(channelId, song, nowPlayingTrack.playStartDate());
        }
    }

    @Override
    public List<AudioChannel> getAllChannels() {
        return audioChannelDao.findAll();
    }

    @Override
    public void skipNowPlayingTrack(long channelId, String reason) {
        audioChannelDao.updatePlayedTrackPlaybackInfo(channelId, reason);
    }

    private void publishAudioTrackIsNowPlayingEvent(long channelId, long audioTrackId) {
        publisher.publishEvent(new AudioChannelTrackIsNowPlayingEvent(this, channelId, audioTrackId));
    }

    private AudioTrack selectNextTrackToPlay(AudioChannel channel) {
        Optional<AudioTrack> nextTrack = findRandomGenreSongNotYetStreamedByChannelId(channel.id(), channel.sources());
        if (nextTrack.isEmpty() && audioChannelConfiguration.isRepeatAll()) {
            audioChannelDao.deletePlayedTracksByChannelId(channel.id());
            nextTrack = findRandomGenreSongNotYetStreamedByChannelId(channel.id(), channel.sources());
        }
        return nextTrack.orElseThrow(() -> PlayqdException.objectDoesNotExistException(channel.sources(), "GenreSong"));
    }

    private Optional<AudioTrack> findRandomGenreSongNotYetStreamedByChannelId(long channelId, Collection<String> genres) {
        return audioTrackDao.findRandomGenreTrackNotYetPlayedByChannelId(channelId, genres);
    }
}
