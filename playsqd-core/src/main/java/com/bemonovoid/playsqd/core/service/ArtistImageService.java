package com.bemonovoid.playsqd.core.service;

import java.util.Optional;

public interface ArtistImageService {

    Optional<String> getImageUrl(String artistId);
}
