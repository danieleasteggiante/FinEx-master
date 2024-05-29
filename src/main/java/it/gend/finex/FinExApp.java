package it.gend.finex;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static it.gend.finex.LoggerClass.javafxLogger;

public class FinExApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            showError(throwable);
        });
        FXMLLoader fxmlLoader = new FXMLLoader(FinExApp.class.getResource("baseController-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Fin.Ex.");
        stage.setScene(scene);
        stage.show();
    }

    private void showError(Throwable throwable) {
        Throwable cause = getCausaErrore(throwable);
        cause.printStackTrace();
        javafxLogger.severe(cause.getMessage());
        // Optionally, show an error dialog to the user
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText(cause.getMessage());
            alert.showAndWait();
        });
    }

    private Throwable getCausaErrore(Throwable throwable) {
        Throwable cause;
        if(throwable.getCause() instanceof InvocationTargetException && throwable.getCause().getCause() != null)
            cause = throwable.getCause().getCause();
        else
            cause = throwable;
        return cause;
    }

    public static void main(String[] args) {
        launch();
    }
}