package com.bemonovoid.playsqd.core.utils;

public abstract class FileUtils {

    public static String fileNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}
