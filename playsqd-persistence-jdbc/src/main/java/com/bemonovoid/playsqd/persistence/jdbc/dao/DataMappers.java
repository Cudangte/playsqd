package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.audio.AudioFile;
import com.bemonovoid.playsqd.core.model.LibraryItemInfo;
import com.bemonovoid.playsqd.persistence.jdbc.entity.LibraryItemEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDateTime;

public abstract class DataMappers {

    static SqlParameterSource audioFileToSqlParametersSource(LibraryItemInfo libraryItemInfo) {
        AudioFile audioFile = libraryItemInfo.item();
        String artistId = libraryItemInfo.metadata().get("artistId");
        String albumId = libraryItemInfo.metadata().get("albumId");

        return new MapSqlParameterSource()

                .addValue(LibraryItemEntity.COL_ARTIST_ID, artistId)
                .addValue(LibraryItemEntity.COL_ARTIST_NAME, audioFile.getArtistName())
                .addValue(LibraryItemEntity.COL_ARTIST_COUNTRY, audioFile.getArtistCountry())

                .addValue(LibraryItemEntity.COL_ALBUM_ID, albumId)
                .addValue(LibraryItemEntity.COL_ALBUM_NAME, audioFile.getAlbumName())
                .addValue(LibraryItemEntity.COL_ALBUM_GENRE, audioFile.getAlbumGenre())
                .addValue(LibraryItemEntity.COL_ALBUM_YEAR, audioFile.getAlbumYear())

                .addValue(LibraryItemEntity.COL_TRACK_NAME, audioFile.getTrackName())
                .addValue(LibraryItemEntity.COL_TRACK_COMMENT, audioFile.getComment())
                .addValue(LibraryItemEntity.COL_TRACK_LYRICS, audioFile.getLyrics())
                .addValue(LibraryItemEntity.COL_TRACK_ID, audioFile.getTrackId())
                .addValue(LibraryItemEntity.COL_TRACK_LENGTH, audioFile.getTrackLength())

                .addValue(LibraryItemEntity.COL_AUDIO_BIT_RATE, audioFile.getBitRate())
                .addValue(LibraryItemEntity.COL_AUDIO_CHANNEL_TYPE, audioFile.getChannels())
                .addValue(LibraryItemEntity.COL_AUDIO_ENCODING_TYPE, audioFile.getEncodingType())
                .addValue(LibraryItemEntity.COL_AUDIO_SAMPLE_RATE, audioFile.getSampleRate())

                .addValue(LibraryItemEntity.COL_FILE_NAME, audioFile.getFileName())
                .addValue(LibraryItemEntity.COL_FILE_LOCATION, audioFile.getFileLocation())
                .addValue(LibraryItemEntity.COL_FILE_EXTENSION, audioFile.getFileExtension())

                .addValue(LibraryItemEntity.COL_MISC_IS_FAVORITE, false)
                .addValue(LibraryItemEntity.COL_MISC_PLAY_COUNT, 0)

                .addValue(LibraryItemEntity.COL_CREATED_BY, "system")
                .addValue(LibraryItemEntity.COL_CREATED_DATE, LocalDateTime.now());
    }
}
