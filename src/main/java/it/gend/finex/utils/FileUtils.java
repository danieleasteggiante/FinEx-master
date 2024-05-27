package it.gend.finex.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static String getFileNameWithoutExtension(String path) {
        return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
    }

    public static String getFileExtension(String path) {
        return path.substring(path.lastIndexOf("."));
    }

    public List<String> reader(String path) throws IOException {
        List<String> list = null;
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            list = lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new IOException("Errore durante la lettura del file", e);
        }
        return list;
    }
}
