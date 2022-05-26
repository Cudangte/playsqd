package com.bemonovoid.playsqd.persistence.jdbc.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor(onConstructor=@__({@PersistenceConstructor}))
@Getter
@Table(AudioTrackEntity.TABLE_NAME)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AudioTrackEntity extends AuditableEntity {

    public static final String TABLE_NAME = "AUDIO_TRACK";

    public static final String COL_ID = "ID";

    public static final String COL_ARTIST_ID = "ARTIST_ID";
    public static final String COL_ARTIST_NAME = "ARTIST_NAME";
    public static final String COL_ARTIST_COUNTRY = "ARTIST_COUNTRY";
    public static final String COL_ARTIST_SPOTIFY_ID = "ARTIST_SPOTIFY_ID";
    public static final String COL_ARTIST_SPOTIFY_NAME = "ARTIST_SPOTIFY_NAME";

    public static final String COL_ALBUM_ID = "ALBUM_ID";
    public static final String COL_ALBUM_NAME = "ALBUM_NAME";
    public static final String COL_ALBUM_YEAR = "ALBUM_YEAR";
    public static final String COL_ALBUM_GENRE = "ALBUM_GENRE";
    public static final String COL_ALBUM_ARTWORK_URL = "ALBUM_ARTWORK_URL";

    public static final String COL_TRACK_NAME = "TRACK_NAME";
    public static final String COL_TRACK_NUMBER = "TRACK_NUMBER";
    public static final String COL_TRACK_LENGTH = "TRACK_LENGTH";
    public static final String COL_TRACK_COMMENT = "TRACK_COMMENT";
    public static final String COL_TRACK_LYRICS = "TRACK_LYRICS";

    public static final String COL_AUDIO_BIT_RATE = "AUDIO_BIT_RATE";
    public static final String COL_AUDIO_CHANNEL_TYPE = "AUDIO_CHANNEL_TYPE";
    public static final String COL_AUDIO_ENCODING_TYPE = "AUDIO_ENCODING_TYPE";
    public static final String COL_AUDIO_SAMPLE_RATE = "AUDIO_SAMPLE_RATE";

    public static final String COL_FILE_NAME = "FILE_NAME";
    public static final String COL_FILE_EXTENSION = "FILE_EXTENSION";
    public static final String COL_FILE_LOCATION = "FILE_LOCATION";

    public static final String COL_MISC_PLAY_COUNT = "MISC_PLAY_COUNT";
    public static final String COL_MISC_IS_FAVORITE = "MISC_IS_FAVORITE";

    @Id
    @Column(COL_ID)
    private final long id;

    @Column(COL_ARTIST_ID)
    private String artistId;

    @Column(COL_ARTIST_NAME)
    private String artistName;

    @Column(COL_ARTIST_COUNTRY)
    private String artistCountry;

    @Column(COL_ARTIST_SPOTIFY_ID)
    private String artistSpotifyId;

    @Column(COL_ARTIST_SPOTIFY_NAME)
    private String artistSpotifyName;

    @Column(COL_ALBUM_ID)
    private String albumId;

    @Column(COL_ALBUM_NAME)
    private String albumName;

    @Column(COL_ALBUM_YEAR)
    private String albumYear;

    @Column(COL_ALBUM_GENRE)
    private String albumGenre;

    @Column(COL_ALBUM_ARTWORK_URL)
    private String albumArtworkUrl;

    @Column(COL_TRACK_NAME)
    private String trackName;

    @Column(COL_TRACK_NUMBER)
    private int trackNumber;

    @Column(COL_TRACK_LENGTH)
    private int trackLength;

    @Column(COL_TRACK_COMMENT)
    private String comment;

    @Column(COL_TRACK_LYRICS)
    private String lyrics;

    @Column(COL_AUDIO_BIT_RATE)
    private String audioBitRate;

    @Column(COL_AUDIO_CHANNEL_TYPE)
    private String audioChannelType;

    @Column(COL_AUDIO_ENCODING_TYPE)
    private String audioEncodingType;

    @Column(COL_AUDIO_SAMPLE_RATE)
    private String audioSampleRate;

    @Column(COL_FILE_NAME)
    private String fileName;

    @Column(COL_FILE_EXTENSION)
    private String fileExtension;

    @Column(COL_FILE_LOCATION)
    private String fileLocation;

    @Column(COL_MISC_PLAY_COUNT)
    private int playCount;

    @Column(COL_MISC_IS_FAVORITE)
    private boolean favorite;
}
