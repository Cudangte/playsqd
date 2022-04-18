package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.dao.AudioChannelDao;
import com.bemonovoid.playsqd.core.model.Song;
import com.bemonovoid.playsqd.core.model.channel.AudioChannel;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelPlaybackItem;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelSelection;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelState;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelStreamingInfo;
import com.bemonovoid.playsqd.core.model.channel.NewAudioChannelData;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioChannelEntity;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioChannelStreamInfoEntity;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioChannelPlaybackEntity;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AuditingEntity;
import com.bemonovoid.playsqd.persistence.jdbc.repository.AudioChannelRepository;
import com.bemonovoid.playsqd.persistence.jdbc.repository.AudioChannelStreamInfoRepository;
import com.bemonovoid.playsqd.persistence.jdbc.repository.AudioChannelStreamPlaybackRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
record AudioChannelDaoImpl(AudioChannelRepository channelRepository,
                           AudioChannelStreamInfoRepository streamInfoRepository,
                           AudioChannelStreamPlaybackRepository playbackRepository) implements AudioChannelDao {

    @Override
    public AudioChannel createChannel(NewAudioChannelData channelData) {
        var entity = new AudioChannelEntity(
                null,
                channelData.name(),
                channelData.description(),
                AudioChannelState.NEW,
                channelData.type(),
                AudioChannelSelection.RANDOM,
                channelData.source(),
                AuditingEntity.created());
        return fromChannelEntity(channelRepository.save(entity));
    }

    @Override
    public void modifyChannelState(long channelId, AudioChannelState newState) {
        channelRepository.setNewState(channelId, newState, LocalDateTime.now());
    }

    @Override
    public Optional<AudioChannel> findChannelById(long channelId) {
        return channelRepository.findById(channelId).map(this::fromChannelEntity);
    }

    @Override
    public List<AudioChannel> findAllChannels() {
        Iterator<AudioChannelEntity> iterator = channelRepository.findAll().iterator();
        return Stream.generate(() -> null)
                .takeWhile(x -> iterator.hasNext())
                .map(x -> iterator.next())
                .map(this::fromChannelEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<AudioChannelPlaybackItem> finaAllChannelPlaybackSongs(long channelId) {
        return playbackRepository.finaAllChannelPlaybackSongs(channelId);
    }

    @Override
    public Optional<AudioChannelStreamingInfo> findChannelStreamInfoByChannelId(long channelId) {
        return streamInfoRepository.findByChannelId(channelId)
                .map(this::fromChannelStreamInfo);
    }

    @Override
    public AudioChannelStreamingInfo createChannelStreamInfo(long channelId, Song song) {
        return fromChannelStreamInfo(streamInfoRepository.save(new AudioChannelStreamInfoEntity(
                null,
                channelId,
                song.getId(),
                LocalDateTime.now(),
                song.getTrackLengthInSeconds(),
                AuditingEntity.created())));
    }

    @Override
    public AudioChannelStreamingInfo updateChannelStreamInfo(long channelId, Song song) {
        streamInfoRepository.setStreamingItemId(
                channelId, song.getId(), LocalDateTime.now(), song.getTrackLengthInSeconds());
        return findChannelStreamInfoByChannelIdOrThrow(channelId);
    }

    @Override
    public void addStreamedItemToHistory(long channelId, long streamedItemId) {
        var entity = new AudioChannelPlaybackEntity(null,
                channelId,
                streamedItemId,
                AuditingEntity.created());
        playbackRepository.save(entity);
    }

    @Override
    public void deletePlaybackHistory(long channelId) {
        playbackRepository.deleteAllByChannelId(channelId);
    }

    private AudioChannel fromChannelEntity(AudioChannelEntity entity) {
        return new AudioChannel(
                entity.id(),
                entity.name(),
                entity.description(),
                entity.state(),
                entity.type(),
                entity.itemSelection(),
                entity.source());
    }

    private AudioChannelStreamingInfo fromChannelStreamInfo(AudioChannelStreamInfoEntity entity) {
        return new AudioChannelStreamingInfo(
                entity.id(),
                entity.channelId(),
                entity.streamingItemId(),
                entity.steamingTimeStamp(),
                entity.streamingItemLengthInSec());
    }
}
