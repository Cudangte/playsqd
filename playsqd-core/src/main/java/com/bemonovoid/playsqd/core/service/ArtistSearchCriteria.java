package com.bemonovoid.playsqd.core.service;

public record ArtistSearchCriteria(String nameLike, String nameStartsWith, PageableInfo pageableInfo) {
}
