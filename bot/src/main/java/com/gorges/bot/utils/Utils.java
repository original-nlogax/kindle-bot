package com.gorges.bot.utils;

import com.gorges.bot.core.Config;
import nl.siegmann.epublib.domain.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isText (String text) {
        return matches (text, "[a-zA-Z]|[U+0400–U+04FF]");
    }

    public static boolean isNumber (String text) {
        return matches (text, "[0-9]+$");
    }

    public static boolean matches (String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(text).find();
    }

    public static String removeForbiddenFilenameCharacters (String filename) {
        return filename.replaceAll("[<>:\"/?*\\\\]", "");
    }

    public static boolean isValidURL (String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static File getResourcesPath (String path) {
        URL url = Config.class.getClassLoader().getResource(path);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }

        return file;
    }

    public static InputStream getResource (String path) {
        return Config.class.getClassLoader().getResourceAsStream(path);
    }

    public static Resource getResource (String path, String href) {
        try {
            return new Resource (getResource (path), href);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFileExtension(String file) {
        int lastIndexOf = file.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return file.substring(lastIndexOf + 1);
    }
}
