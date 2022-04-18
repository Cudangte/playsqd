package com.bemonovoid.playsqd.core.utils;

import java.io.File;

public abstract class FileUtils {

    public static String fileNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     *
     * @param file
     * @return name of the file or empty string if file name was unavailable
     */
    public static String getFileExtension(File file) {
        int i = file.getName().lastIndexOf(".");
        return i == -1 ? "" : file.getName().toLowerCase().substring(i + 1);
    }
}
