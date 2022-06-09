package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.dao.AudioChannelDao;
import com.bemonovoid.playsqd.core.model.AudioTrack;
import com.bemonovoid.playsqd.core.model.channel.AudioChannel;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelPlayedTrack;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelSelection;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelState;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelNowPlayingTrack;
import com.bemonovoid.playsqd.core.model.channel.NewAudioChannelData;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioChannelEntity;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioChannelNowPlayingTrackEntity;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioChannelPlayedTrackEntity;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AuditingEntity;
import com.bemonovoid.playsqd.persistence.jdbc.repository.AudioChannelRepository;
import com.bemonovoid.playsqd.persistence.jdbc.repository.AudioChannelNowPlayingTrackRepository;
import com.bemonovoid.playsqd.persistence.jdbc.repository.AudioChannelPlayedTrackRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
record AudioChannelDaoImpl(AudioChannelRepository channelRepository,
                           AudioChannelNowPlayingTrackRepository channelNowPlayingRepository,
                           AudioChannelPlayedTrackRepository channelPlayedTrackRepository) implements AudioChannelDao {

    @Override
    public AudioChannel createChannel(NewAudioChannelData channelData) {
        var entity = new AudioChannelEntity(
                null,
                channelData.name(),
                channelData.description(),
                AudioChannelState.NEW,
                channelData.type(),
                channelData.repeat(),
                AudioChannelSelection.RANDOM,
                String.join(",", channelData.sources()),
                AuditingEntity.created());
        return fromChannelEntity(channelRepository.save(entity));
    }

    @Override
    public void updateState(long channelId, AudioChannelState newState) {
        channelRepository.setNewState(channelId, newState, LocalDateTime.now());
    }

    @Override
    public Optional<AudioChannel> findById(long channelId) {
        return channelRepository.findById(channelId).map(this::fromChannelEntity);
    }

    @Override
    public List<AudioChannel> findAll() {
        Iterator<AudioChannelEntity> iterator = channelRepository.findAll().iterator();
        return Stream.generate(() -> null)
                .takeWhile(x -> iterator.hasNext())
                .map(x -> iterator.next())
                .map(this::fromChannelEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<AudioChannelPlayedTrack> findPlayedTracks(long channelId) {
        return channelPlayedTrackRepository.findAudioTracksByChannelId(channelId);
    }

    @Override
    public Optional<AudioChannelNowPlayingTrack> findNowPLayingTrackByChannelId(long channelId) {
        return channelNowPlayingRepository.findByChannelId(channelId)
                .map(this::fromChannelNowPlayingEntity);
    }

    @Override
    public AudioChannelNowPlayingTrack createNowPlayingTrack(long channelId, AudioTrack audioTrack) {
        return fromChannelNowPlayingEntity(channelNowPlayingRepository.save(new AudioChannelNowPlayingTrackEntity(
                null,
                channelId,
                audioTrack.id(),
                audioTrack.trackLengthInSeconds(),
                LocalDateTime.now(),
                AuditingEntity.created())));
    }

    @Override
    public AudioChannelNowPlayingTrack updateNowPlayingTrack(long channelId, AudioTrack audioTrack) {
        channelNowPlayingRepository.updateNowPlayingTrack(
                channelId, audioTrack.id(), audioTrack.trackLengthInSeconds(), LocalDateTime.now());
        return findChannelStreamInfoByChannelIdOrThrow(channelId);
    }

    @Override
    public void setAudioTrackAsPlayed(long channelId, long audioTrackId) {
        var entity = new AudioChannelPlayedTrackEntity(
                null,
                channelId,
                audioTrackId,
                null,
                AuditingEntity.created());
        channelPlayedTrackRepository.save(entity);
    }

    @Override
    public void deletePlayedTracksByChannelId(long channelId) {
        channelPlayedTrackRepository.deleteAllByChannelId(channelId);
    }

    @Override
    public void updatePlayedTrackPlaybackInfo(long channelId, String playbackInfo) {
        findNowPLayingTrackByChannelId(channelId)
                .map(AudioChannelNowPlayingTrack::audioTrackId)
                .ifPresent(trackId ->
                        channelPlayedTrackRepository.setPlayedTrackPlaybackInfo(
                                channelId,
                                trackId,
                                playbackInfo));

    }

    private AudioChannel fromChannelEntity(AudioChannelEntity entity) {
        return new AudioChannel(
                entity.id(),
                entity.name(),
                entity.description(),
                entity.state(),
                entity.type(),
                entity.itemSelection(),
                entity.repeat(),
                Arrays.stream(entity.source().split(",")).toList());
    }

    private AudioChannelNowPlayingTrack fromChannelNowPlayingEntity(AudioChannelNowPlayingTrackEntity entity) {
        return new AudioChannelNowPlayingTrack(
                entity.id(),
                entity.channelId(),
                entity.audioTrackId(),
                entity.audioTrackLengthInSec(),
                entity.playStartDate());
    }
}
