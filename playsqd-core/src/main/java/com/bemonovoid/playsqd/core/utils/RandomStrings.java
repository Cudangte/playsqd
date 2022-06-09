package com.bemonovoid.playsqd.core.utils;

import org.apache.commons.lang3.RandomStringUtils;

public abstract class RandomStrings {

    public static String randomAlphabetic(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }
}
