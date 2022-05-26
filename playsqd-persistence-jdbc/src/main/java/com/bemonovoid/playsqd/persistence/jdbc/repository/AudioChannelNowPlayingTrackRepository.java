package com.bemonovoid.playsqd.persistence.jdbc.repository;

import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioChannelNowPlayingTrackEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AudioChannelNowPlayingTrackRepository extends CrudRepository<AudioChannelNowPlayingTrackEntity, Long> {

    Optional<AudioChannelNowPlayingTrackEntity> findByChannelId(long channelId);

    @Modifying
    @Query("UPDATE AUDIO_CHANNEL_NOW_PLAYING_TRACK " +
            "SET " +
                "AUDIO_TRACK_ID = :audioTrackId, " +
                "PLAY_START_DATE = :playStartDate, " +
                "AUDIO_TRACK_LENGTH_IN_SEC = :trackLengthInSec " +
            "WHERE AUDIO_CHANNEL_ID = :channelId")
    boolean updateNowPlayingTrack(@Param("channelId") long channelId,
                                  @Param("audioTrackId") long audioTrackId,
                                  @Param("trackLengthInSec") int trackLengthInSec,
                                  @Param("playStartDate") LocalDateTime playStartDate);
}
