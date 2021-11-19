package com.bemonovoid.playsqd.integration.spotify.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter(AccessLevel.PACKAGE)
@Validated
public class SpotifyProperties {

    private boolean enabled;

    @NonNull
    private String apiBaseUrl = "https://api.spotify.com";

    @NonNull
    private String accountBaseUrl = "https://accounts.spotify.com";

    @NonNull
    private String apiVersion = "v1";

    private String clientId;

    private String clientSecret;
}
