package com.bemonovoid.playsqd.core.dao;

import com.bemonovoid.playsqd.core.exception.DatabaseItemNotFoundException;
import com.bemonovoid.playsqd.core.model.MediaDirectory;

import java.util.Collection;

public interface MediaDirectoryDao {

    /**
     *
     * @param directoryId
     * @return
     * @throws DatabaseItemNotFoundException
     */
    MediaDirectory getMediaDirectory(long directoryId);

    long add(MediaDirectory mediaDirectory);

    Collection<MediaDirectory> getAll();

    boolean existsWithPath(String path);
}
