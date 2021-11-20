package com.bemonovoid.playsqd.core.service.impl;

import com.bemonovoid.playsqd.core.dao.MediaLibraryDao;
import com.bemonovoid.playsqd.core.service.LibraryEditorService;
import org.springframework.stereotype.Component;

@Component
class LibraryEditorServiceImpl implements LibraryEditorService {

    private final MediaLibraryDao mediaLibraryDao;

    public LibraryEditorServiceImpl(MediaLibraryDao mediaLibraryDao) {
        this.mediaLibraryDao = mediaLibraryDao;
    }

    @Override
    public void updateFavoriteStatus(long songId) {
        mediaLibraryDao.updateFavoriteStatus(songId);
    }
}
