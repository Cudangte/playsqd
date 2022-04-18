package com.bemonovoid.playsqd.config;

import com.bemonovoid.playsqd.core.config.properties.AudioChannelConfiguration;
import com.bemonovoid.playsqd.core.config.properties.LibrarySourceConfigProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@EnableConfigurationProperties
class PlayqdApplicationConfiguration {

    @Bean
    LibrarySourceConfigProperties musicLibrarySourceConfigProperties() {
        return new LibrarySourceConfigProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "playqd.channel")
    AudioChannelConfiguration audioChannelConfiguration() {
        return new AudioChannelConfiguration();
    }

}
