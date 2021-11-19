package com.bemonovoid.playsqd.integration.spotify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SpotifyConfig {

    @Bean
    SpotifyProperties spotifyProperties() {
        return new SpotifyProperties();
    }
}
