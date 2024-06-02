package it.gend.finex.utils;

import javafx.scene.control.Alert;

/**
 * @author Daniele Asteggiante
 */
public class ControllerUtils {
    public static Alert showAlert(Alert.AlertType alertType, String message) {
        Alert a = new Alert(alertType);
        a.setContentText(message);
        return a;
    }
}
