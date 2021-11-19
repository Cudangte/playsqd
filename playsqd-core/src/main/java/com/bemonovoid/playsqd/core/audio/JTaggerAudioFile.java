package com.bemonovoid.playsqd.core.audio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Getter
@AllArgsConstructor
public class JTaggerAudioFile implements AudioFile {

    private static final String UNKNOWN_ARTIST = "Unknown artist";
    private static final String UNKNOWN_ALBUM = "Unknown album";

    private final org.jaudiotagger.audio.AudioFile jTaggerAudioFile;

    @Override
    public String getArtistName() {
        String name;
        name = readFromTag(FieldKey.ARTIST);
        if (!StringUtils.hasText(name)) {
            name = readFromTag(FieldKey.ALBUM_ARTIST);
        }
        if (!StringUtils.hasText(name)) {
            name = readFromTag(FieldKey.ORIGINAL_ARTIST);
        }
        if (!StringUtils.hasText(name)) {
            name = readFromTag(FieldKey.COMPOSER);
        }
        if (!StringUtils.hasText(name)) {
            name = UNKNOWN_ARTIST;
        }
        return name;
    }

    @Override
    public String getArtistCountry() {
        return readFromTag(FieldKey.COUNTRY);
    }

    @Override
    public String getAlbumName() {
        String name = readFromTag(FieldKey.ALBUM);
        if (StringUtils.hasText(name)) {
            return name.trim();
        }
        return UNKNOWN_ALBUM;
    }

    @Override
    public String getSongName() {
        String songName = readFromTag(FieldKey.TITLE);
        if (!StringUtils.hasText(songName)) {
            songName = getFileName();
        }
        return songName;
    }

    @Override
    public String getGenre() {
        return readFromTag(FieldKey.GENRE);
    }

    @Override
    public String getYear() {
        return readFromTag(FieldKey.YEAR);
    }

    @Override
    public String getComment() {
        return readFromTag(FieldKey.COMMENT);
    }

    @Override
    public String getLyrics() {
        return readFromTag(FieldKey.LYRICS);
    }

    @Override
    public int getTrackId() {
        String trackId = readFromTag(FieldKey.TRACK);
        if (StringUtils.hasText(trackId)) {
            String resolvedTrackId = trackId;
            if (resolvedTrackId.length() > 1 && resolvedTrackId.startsWith("0")) {
                resolvedTrackId = resolvedTrackId.replaceFirst("0", "");
            }
            return Integer.parseInt(resolvedTrackId);
        }
        return 0;
    }

    @Override
    public String getFileName() {
        String fileName = jTaggerAudioFile.getFile().getName();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    @Override
    public String getFileLocation() {
        return jTaggerAudioFile.getFile().getAbsolutePath();
    }

    @Override
    public String getFileExtension() {
        return jTaggerAudioFile.getExt();
    }

    @Override
    public String getEncodingType() {
        return jTaggerAudioFile.getAudioHeader().getEncodingType();
    }

    @Override
    public String getSampleRate() {
        return jTaggerAudioFile.getAudioHeader().getSampleRate();
    }

    @Override
    public String getBitRate() {
        return jTaggerAudioFile.getAudioHeader().getBitRate();
    }

    @Override
    public String getChannels() {
        return jTaggerAudioFile.getAudioHeader().getChannels();
    }

    @Override
    public int getTrackLength() {
        return jTaggerAudioFile.getAudioHeader().getTrackLength();
    }

    @Override
    public Optional<byte[]> getArtwork() {
        Tag tag = jTaggerAudioFile.getTag();
        if (tag != null && tag.getFirstArtwork() != null) {
            return Optional.ofNullable(tag.getFirstArtwork().getBinaryData());
        }
        return Optional.empty();
    }

    private String readFromTag(FieldKey key) {
        try  {
            return Optional.ofNullable(jTaggerAudioFile.getTag())
                    .map(tag -> tag.getFirst(key))
                    .map(String::trim)
                    .orElse(null);
        } catch (UnsupportedOperationException e) {
            log.error("Failed to read {} tag from path {}. \n {}",
                    key, jTaggerAudioFile.getFile().getAbsolutePath(), e.getMessage());
            return null;
        }
    }
}
