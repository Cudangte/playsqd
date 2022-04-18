CREATE TABLE AUDIO_SOURCE
(
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) primary key,

    NAME VARCHAR(255),
    PATH VARCHAR(255),

    AUTO_SCAN_ON_CREATE BOOLEAN,
    AUTO_SCAN_ON_RESTART BOOLEAN,
    DELETE_MISSING BOOLEAN,
    DELETE_ALL_BEFORE_SCAN BOOLEAN,
    STATUS VARCHAR(255),
    STATUS_DETAILS VARCHAR(255),
    LAST_SCAN_DATE TIMESTAMP,

    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_BY VARCHAR(255),
    LAST_MODIFIED_DATE TIMESTAMP
);

CREATE INDEX AUDIO_SOURCE_IDX
    ON AUDIO_SOURCE (NAME, PATH);

CREATE TABLE AUDIO_SOURCE_LOG
(
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) primary key,

    SOURCE_ID INTEGER,
    ITEMS_SCANNED INTEGER,
    ITEMS_MISSING INTEGER,
    SCAN_DURATION_IN_MILLIS INTEGER,

    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_BY VARCHAR(255),
    LAST_MODIFIED_DATE TIMESTAMP
);

CREATE TABLE LIBRARY_ITEM
(
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) primary key,

    ARTIST_ID VARCHAR(255),
    ARTIST_SPOTIFY_ID VARCHAR(255),
    ARTIST_SPOTIFY_NAME VARCHAR(255),
    ARTIST_NAME VARCHAR(255),
    ARTIST_COUNTRY VARCHAR(255),

    ALBUM_ID VARCHAR(255),
    ALBUM_NAME VARCHAR(255),
    ALBUM_YEAR VARCHAR(255),
    ALBUM_GENRE VARCHAR(255),

    TRACK_NAME VARCHAR(1000),
    TRACK_ID INTEGER,
    TRACK_LENGTH INTEGER,
    TRACK_COMMENT VARCHAR(3000),
    TRACK_LYRICS VARCHAR(16777216),

    AUDIO_BIT_RATE VARCHAR(255),
    AUDIO_CHANNEL_TYPE VARCHAR(255),
    AUDIO_ENCODING_TYPE VARCHAR(255),
    AUDIO_SAMPLE_RATE VARCHAR(255),

    FILE_NAME VARCHAR(255),
    FILE_EXTENSION VARCHAR(255),
    FILE_LOCATION VARCHAR(255),

    MISC_PLAY_COUNT INTEGER,
    MISC_IS_FAVORITE BOOLEAN,

    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_BY VARCHAR(255),
    LAST_MODIFIED_DATE TIMESTAMP
);

CREATE INDEX LIBRARY_ITEM_IDX
	ON LIBRARY_ITEM (ARTIST_ID, ARTIST_NAME, ALBUM_ID, ALBUM_NAME, ALBUM_GENRE, TRACK_NAME, FILE_NAME);

CREATE TABLE AUDIO_CHANNEL
(
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key,

    NAME VARCHAR(255),
    DESCRIPTION VARCHAR(255),
    STATE VARCHAR(50),
    TYPE VARCHAR(255),
    SOURCE VARCHAR(255),
    ITEM_SELECTION VARCHAR(255),

    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_BY VARCHAR(255),
    LAST_MODIFIED_DATE TIMESTAMP
);

CREATE INDEX AUDIO_CHANNEL_IDX
    ON AUDIO_CHANNEL (NAME);

CREATE TABLE AUDIO_CHANNEL_STREAM_INFO
(
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key,

    AUDIO_CHANNEL_ID INTEGER,
    STREAMING_ITEM_ID INTEGER,
    STREAMING_TIMESTAMP TIMESTAMP,
    STREAMING_ITEM_LENGTH_IN_SEC INTEGER,

    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_BY VARCHAR(255),
    LAST_MODIFIED_DATE TIMESTAMP
);

CREATE INDEX AUDIO_CHANNEL_STREAM_INFO_IDX
    ON AUDIO_CHANNEL_STREAM_INFO (AUDIO_CHANNEL_ID);

CREATE TABLE AUDIO_CHANNEL_PLAYBACK_HISTORY
(
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key,

    AUDIO_CHANNEL_ID INTEGER,
    STREAMED_ITEM_ID INTEGER,

    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_BY VARCHAR(255),
    LAST_MODIFIED_DATE TIMESTAMP
);

CREATE INDEX AUDIO_CHANNEL_PLAYBACK_HISTORY_IDX
    ON AUDIO_CHANNEL_PLAYBACK_HISTORY (AUDIO_CHANNEL_ID);