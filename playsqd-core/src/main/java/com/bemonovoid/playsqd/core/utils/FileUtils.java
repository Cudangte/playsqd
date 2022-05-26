package com.bemonovoid.playsqd.core.utils;

import com.bemonovoid.playsqd.core.model.Tuple;

import java.io.File;
import java.util.Set;

public abstract class FileUtils {

    public static final Set<String> SUPPORTED_AUDIO_EXTENSIONS =
            Set.of("flac", "m4a", "m4p", "mp3", "ogg", "wav", "wma");

    public static boolean isSupportedAudioFile(File file) {
        return SUPPORTED_AUDIO_EXTENSIONS.contains(getFileExtension(file));
    }

    public static String fileNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     *
     * @param file
     * @return name of the file or empty string if file name was unavailable
     */
    public static String getFileExtension(File file) {
        return getFileExtension(file.getName());
    }

    public static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf(".");
        return i == -1 ? "" : fileName.toLowerCase().substring(i + 1);
    }

    public static Tuple<String, String> parseFileNameAndExtension(String fileName) {
        var extensionSeparatorIdx = fileName.lastIndexOf(".");
        var name = fileName.substring(0, extensionSeparatorIdx);
        var extension = fileName.substring(extensionSeparatorIdx + 1);
        return new Tuple<>(name, extension);
    }
}
