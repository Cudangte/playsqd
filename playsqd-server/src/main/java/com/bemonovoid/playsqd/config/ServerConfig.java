package com.bemonovoid.playsqd.config;

import com.bemonovoid.playsqd.core.config.properties.LibrarySourceConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@EnableConfigurationProperties
class ServerConfig {

    @Bean
    LibrarySourceConfigProperties musicLibrarySourceConfigProperties() {
        return new LibrarySourceConfigProperties();
    }

}
