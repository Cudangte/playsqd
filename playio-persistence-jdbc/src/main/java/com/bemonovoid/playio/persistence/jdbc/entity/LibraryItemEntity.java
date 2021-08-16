package com.bemonovoid.playio.persistence.jdbc.entity;

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
@Table(LibraryItemEntity.TABLE_NAME)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LibraryItemEntity extends AuditableEntity {

    public static final String TABLE_NAME = "LIBRARY_ITEM";

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

    public static final String COL_SONG_NAME = "SONG_NAME";
    public static final String COL_SONG_TRACK_ID = "SONG_TRACK_ID";
    public static final String COL_SONG_TRACK_LENGTH = "SONG_TRACK_LENGTH";
    public static final String COL_SONG_COMMENT = "SONG_COMMENT";
    public static final String COL_SONG_LYRICS = "SONG_LYRICS";

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

    @Column(COL_SONG_NAME)
    private String songName;

    @Column(COL_SONG_TRACK_ID)
    private int songTrackId;

    @Column(COL_SONG_TRACK_LENGTH)
    private int songTrackLength;

    @Column(COL_SONG_COMMENT)
    private String songComment;

    @Column(COL_SONG_LYRICS)
    private String songLyrics;

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
