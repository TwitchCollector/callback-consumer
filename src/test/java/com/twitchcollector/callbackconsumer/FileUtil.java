package com.twitchcollector.callbackconsumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FileUtil {

    public static String fileAsString(String file) {
        final var inputStream = FileUtil.class.getResourceAsStream(file);
        if (inputStream == null) {
            throw new RuntimeException("Could not find file: " + file);
        }
        return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining());
    }
}
