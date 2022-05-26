package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AudioTrack(long id,
                         String name,
                         String comment,
                         String lyrics,
                         boolean favorite,
                         @JsonProperty("track_number") int trackNumber,
                         @JsonProperty("track_length_in_seconds") int trackLengthInSeconds,
                         @JsonProperty("bit_rate") String audioBitRate,
                         @JsonProperty("channel_type") String audioChannelType,
                         @JsonProperty("encoding_type") String audioEncodingType,
                         @JsonProperty("sample_rate") String audioSampleRate,
                         @JsonProperty("extension") String fileExtension,
                         @JsonProperty("file_name") String fileName,
                         @JsonProperty("play_count") int playCount,
                         @JsonIgnore String fileLocation,
                         Artist artist,
                         Album album) {
}
