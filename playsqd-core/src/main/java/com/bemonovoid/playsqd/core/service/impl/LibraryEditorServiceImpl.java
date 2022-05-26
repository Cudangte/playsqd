package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.audio.AudioFileReader;
import com.bemonovoid.playsqd.core.dao.AudioTrackDao;
import com.bemonovoid.playsqd.core.service.LibraryEditorService;
import org.springframework.stereotype.Component;

@Component
public record LibraryEditorServiceImpl(AudioTrackDao audioTrackDao, AudioFileReader audioFileReader)
        implements LibraryEditorService {

    @Override
    public void updateFavoriteStatus(long songId) {
        audioTrackDao.updateFavoriteStatus(songId);
    }



}
