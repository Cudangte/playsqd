package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.model.channel.AudioChannel;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelWithPlaybackInfo;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelTrack;
import com.bemonovoid.playsqd.core.model.channel.NewAudioChannelData;
import com.bemonovoid.playsqd.core.service.AudioChannelService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiEndpoints.CHANNEL_API_PATH)
class AudioChannelController {

    private final AudioChannelService audioChannelService;

    AudioChannelController(AudioChannelService audioChannelService) {
        this.audioChannelService = audioChannelService;
    }

    @PostMapping
    ResponseEntity<Object> createChannel(@RequestBody NewAudioChannelData requestBody) {
        AudioChannel audioChannel = audioChannelService.create(requestBody);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{channelId}")
                .buildAndExpand(audioChannel.id())
                .toUri();
        return ResponseEntity.created(location).body(Map.of("channelId", audioChannel.id()));
    }

    @GetMapping
    List<AudioChannel> audioChannels() {
        return audioChannelService.getAllChannels();
    }

    @GetMapping("/{channelId}")
    AudioChannel audioChannel(@PathVariable long channelId) {
        return audioChannelService.getChannel(channelId);
    }

    @GetMapping("/{channelId}/extended")
    AudioChannelWithPlaybackInfo audioChannelWithPlaybackInfo(@PathVariable long channelId) {
        return audioChannelService.getChannelExtended(channelId);
    }

    @DeleteMapping("/played/{channelId}")
    void deleteChannelPlayedTracks(@PathVariable long channelId) {
        audioChannelService.deleteChannelPlaybackHistory(channelId);
    }

    @GetMapping("/{channelId}/playing")
    AudioChannelTrack audioChannelNowPlayingTrack(@PathVariable long channelId) {
        return audioChannelService.audioChannelNowPlayingTrack(channelId);
    }

    @PatchMapping("/{channelId}/playing")
    void skipNowPlayingTrack(@PathVariable long channelId, @RequestBody Map<String, Object> data) {
        audioChannelService.skipNowPlayingTrack(channelId, data.getOrDefault("reason", "").toString());
    }

}
