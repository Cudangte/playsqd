package com.bemonovoid.playsqd.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AlbumInfo(@JsonProperty("artist_id") String artistid,
                        @JsonProperty("artist_name") String artistname,
                        String id,
                        String name,
                        String year,
                        String genre,
                        @JsonProperty("songs_count") int trackcount,
                        @JsonProperty("total_time_in_seconds") int totaltimeinseconds) {


}
