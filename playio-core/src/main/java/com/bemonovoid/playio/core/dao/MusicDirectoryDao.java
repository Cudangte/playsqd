package com.bemonovoid.playio.core.dao;

import com.bemonovoid.playio.core.exception.DatabaseItemNotFoundException;
import com.bemonovoid.playio.core.model.MusicDirectory;

import java.util.Collection;

public interface MusicDirectoryDao {

    /**
     *
     * @param directoryId
     * @return
     * @throws DatabaseItemNotFoundException
     */
    MusicDirectory getMusicDirectory(long directoryId);

    long add(MusicDirectory musicDirectory);

    Collection<MusicDirectory> getAll();

    boolean existsWithPath(String path);
}
