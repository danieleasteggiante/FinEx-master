package it.gend.finex.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    public static String getFileExtension(String path) {
        return path.substring(path.lastIndexOf("."));
    }
}
