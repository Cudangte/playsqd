package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.dao.MusicLibraryItemDao;
import com.bemonovoid.playsqd.core.service.LibraryEditorService;
import org.springframework.stereotype.Component;

@Component
class LibraryEditorServiceImpl implements LibraryEditorService {

    private final MusicLibraryItemDao musicLibraryItemDao;

    public LibraryEditorServiceImpl(MusicLibraryItemDao musicLibraryItemDao) {
        this.musicLibraryItemDao = musicLibraryItemDao;
    }

    @Override
    public void updateFavoriteStatus(long songId) {
        musicLibraryItemDao.updateFavoriteStatus(songId);
    }
}
