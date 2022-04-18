package com.bemonovoid.playsqd.rest.api.controller;

import com.bemonovoid.playsqd.core.model.channel.AudioChannel;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelListItem;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelPlaybackItem;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelWithPlaybackHistoryItems;
import com.bemonovoid.playsqd.core.model.channel.AudioChannelWithStreamingItemInfo;
import com.bemonovoid.playsqd.core.model.channel.NewAudioChannelData;
import com.bemonovoid.playsqd.core.service.AudioChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    List<AudioChannelListItem> audioChannels() {
        return audioChannelService.getAllChannels().stream()
                .map(audioChannel -> new AudioChannelListItem(audioChannel.id(), audioChannel.name()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{channelId}")
    AudioChannel audioChannel(@PathVariable long channelId) {
        return audioChannelService.getChannel(channelId);
    }

    @GetMapping("/{channelId}/extended")
    AudioChannelWithPlaybackHistoryItems audioChannelWithPlaybackHistory(@PathVariable long channelId) {
        return new AudioChannelWithPlaybackHistoryItems(
                audioChannelService.getChannel(channelId),
                audioChannelService.getChannelPlaybackSongs(channelId));
    }

    @GetMapping("/{channelId}/history")
    Collection<AudioChannelPlaybackItem> channelPlaybackHistory(@PathVariable long channelId) {
        return audioChannelService.getChannelPlaybackSongs(channelId);
    }

    @DeleteMapping("/history/{channelId}")
    void deleteChannelPlaybackHistory(@PathVariable long channelId) {
        audioChannelService.deleteChannelPlaybackHistory(channelId);
    }

    @GetMapping("/{channelId}/stream/info")
    AudioChannelWithStreamingItemInfo audioChannelWithStreamingItemInfo(@PathVariable long channelId) {
        return audioChannelService.audioChannelWithStreamingItemInfo(channelId);
    }

}
