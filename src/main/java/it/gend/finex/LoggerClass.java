package it.gend.finex;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class LoggerClass {
    protected static final java.util.logging.Logger javafxLogger =
            java.util.logging.Logger.getLogger("javafx");

    static {
        if(!deleteFileIfExists("output.log"))
            throw new RuntimeException("Impossibile eliminare il file di log");

        FileHandler fh = null;
        try {
            fh = new FileHandler("output.log", (1048576 * 30), 1000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fh.setFormatter(new SimpleFormatter());
        javafxLogger.addHandler(fh);
    }

    private static boolean deleteFileIfExists(String s) {
        File output = Path.of(s).toFile();
        if(output.exists())
           return output.delete();
        return true;
    }
}
