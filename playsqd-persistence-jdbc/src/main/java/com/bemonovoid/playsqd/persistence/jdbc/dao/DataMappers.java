package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.audio.AudioFile;
import com.bemonovoid.playsqd.core.model.ScannedAudioFileWithMetadata;
import com.bemonovoid.playsqd.persistence.jdbc.entity.AudioTrackEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDateTime;

public abstract class DataMappers {

    static SqlParameterSource audioFileToSqlParametersSource(ScannedAudioFileWithMetadata scannedAudioFileWithMetadata) {
        AudioFile audioFile = scannedAudioFileWithMetadata.item();
        String artistId = scannedAudioFileWithMetadata.metadata().generatedArtistId();
        String albumId = scannedAudioFileWithMetadata.metadata().generatedAlbumId();

        return new MapSqlParameterSource()

                .addValue(AudioTrackEntity.COL_ARTIST_ID, artistId)
                .addValue(AudioTrackEntity.COL_ARTIST_NAME, audioFile.getArtistName())
                .addValue(AudioTrackEntity.COL_ARTIST_COUNTRY, audioFile.getArtistCountry())

                .addValue(AudioTrackEntity.COL_ALBUM_ID, albumId)
                .addValue(AudioTrackEntity.COL_ALBUM_NAME, audioFile.getAlbumName())
                .addValue(AudioTrackEntity.COL_ALBUM_GENRE, audioFile.getAlbumGenre())
                .addValue(AudioTrackEntity.COL_ALBUM_YEAR, audioFile.getAlbumYear())
                .addValue(AudioTrackEntity.COL_ALBUM_ARTWORK_URL, audioFile.getArtwork().imageUrl())

                .addValue(AudioTrackEntity.COL_TRACK_NAME, audioFile.getTrackName())
                .addValue(AudioTrackEntity.COL_TRACK_COMMENT, audioFile.getComment())
                .addValue(AudioTrackEntity.COL_TRACK_LYRICS, audioFile.getLyrics())
                .addValue(AudioTrackEntity.COL_TRACK_NUMBER, audioFile.getTrackId())
                .addValue(AudioTrackEntity.COL_TRACK_LENGTH, audioFile.getTrackLength())

                .addValue(AudioTrackEntity.COL_AUDIO_BIT_RATE, audioFile.getBitRate())
                .addValue(AudioTrackEntity.COL_AUDIO_CHANNEL_TYPE, audioFile.getChannels())
                .addValue(AudioTrackEntity.COL_AUDIO_ENCODING_TYPE, audioFile.getEncodingType())
                .addValue(AudioTrackEntity.COL_AUDIO_SAMPLE_RATE, audioFile.getSampleRate())

                .addValue(AudioTrackEntity.COL_FILE_NAME, audioFile.getFileName())
                .addValue(AudioTrackEntity.COL_FILE_LOCATION, audioFile.getFileLocation())
                .addValue(AudioTrackEntity.COL_FILE_EXTENSION, audioFile.getFileExtension())

                .addValue(AudioTrackEntity.COL_MISC_IS_FAVORITE, false)
                .addValue(AudioTrackEntity.COL_MISC_PLAY_COUNT, 0)

                .addValue(AudioTrackEntity.COL_CREATED_BY, "system")
                .addValue(AudioTrackEntity.COL_CREATED_DATE, LocalDateTime.now());
    }
}
