package it.gend.finex;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class LoggerClass {
    protected static final java.util.logging.Logger javafxLogger =
            java.util.logging.Logger.getLogger("javafx");

    static {
        FileHandler fh = null;
        try {
            fh = new FileHandler("output.log", (1048576 * 30), 1000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fh.setFormatter(new SimpleFormatter());
        javafxLogger.addHandler(fh);
    }

    @FXML
    protected Object getElement(String elementPrefix, String buttonId) throws NoSuchFieldException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(elementPrefix + buttonId.substring(buttonId.length() - 1));
        field.setAccessible(true);
        return field.get(this);
    }

    @FXML
    protected void fillTextField(TextField textField, Path path) {
        textField.setText(path.toString());
    }
}
