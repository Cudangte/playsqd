package com.bemonovoid.playsqd.core.service;

public record AlbumSearchCriteria(String artistId, String albumName, PageableInfo pageableInfo) {

    public static AlbumSearchCriteria pageableByName(String albumName, PageableInfo pageableInfo) {
        return new AlbumSearchCriteria(null, albumName, pageableInfo);
    }

    public static AlbumSearchCriteria forArtistId(String artistId) {
        return new AlbumSearchCriteria(artistId , null, null);
    }
}
