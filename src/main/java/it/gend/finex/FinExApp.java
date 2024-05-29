package it.gend.finex;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static it.gend.finex.LoggerClass.javafxLogger;

public class FinExApp extends Application {
    private ExceptionHandler exceptionHandler;
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void start(Stage stage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if(exceptionHandler != null)
                exceptionHandler.handleException(throwable);
            else
                javafxLogger.severe(throwable.getMessage());
        });
        FXMLLoader fxmlLoader = new FXMLLoader(FinExApp.class.getResource("baseController-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        BaseController controller = fxmlLoader.getController();
        setExceptionHandler(controller);
        stage.setTitle("Fin.Ex.");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}