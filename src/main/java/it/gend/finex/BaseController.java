package it.gend.finex;

import com.opencsv.exceptions.CsvException;
import it.gend.finex.domain.Esame;
import it.gend.finex.domain.Patient;
import it.gend.finex.domain.ProgressTask;
import it.gend.finex.parser.CsvExporter;
import it.gend.finex.parser.Loader;
import it.gend.finex.parser.PatientToCsvString;
import it.gend.finex.utils.ControllerUtils;
import it.gend.finex.utils.FileUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static it.gend.finex.LoggerClass.javafxLogger;

public class BaseController implements ExceptionHandler {
    @FXML
    private ProgressBar pbBar;

    @FXML
    private AnchorPane apRoot;

    @FXML
    private Button btClear1;

    @FXML
    private Button btClear2;

    @FXML
    private Button btClear3;

    @FXML
    private Button btClear4;

    @FXML
    private Button btClear5;

    @FXML
    private Button btFindAndSave;

    @FXML
    private Button btScegli1;

    @FXML
    private Button btScegli2;

    @FXML
    private Button btScegli3;

    @FXML
    private Button btScegli4;

    @FXML
    private Button btScegli5;

    @FXML
    private TextField tfInputFile1;

    @FXML
    private TextField tfInputFile2;

    @FXML
    private TextField tfInputFile3;

    @FXML
    private TextField tfInputFile4;

    @FXML
    private TextField tfInputFile5;

    ObservableList<Path> pathsFileList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        pbBar.setVisible(false);
        pathsFileList.addListener((ListChangeListener<? super Path>) c -> {
            while (c.next()) {
                if (c.wasRemoved() && c.getList().isEmpty())
                    btFindAndSave.setDisable(true);
                if (c.wasAdded() && !c.getList().isEmpty())
                    btFindAndSave.setDisable(false);
            }
        });

    }

    @FXML
    private void openFileAndAddToList(Button button) throws NoSuchFieldException, IllegalAccessException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(apRoot.getScene().getWindow());
        if (file != null && FileUtils.getFileExtension(file.getName()).equals(".csv")) {
            pathsFileList.add(Paths.get(file.getAbsolutePath()));
            button.setDisable(true);
            fillTextField((TextField) getElement("tfInputFile", button.getId()), Paths.get(file.getAbsolutePath()));
            ((Button) getElement("btClear", button.getId())).setDisable(false);
        }
    }


    @FXML
    void searchAndAddFile(MouseEvent event) throws NoSuchFieldException, IllegalAccessException {
        Button clickedButton = (Button) event.getSource();
        openFileAndAddToList(clickedButton);
    }

    @FXML
    void clearFileAndDisable(MouseEvent event) throws NoSuchFieldException, IllegalAccessException {
        Button clickedButton = (Button) event.getSource();
        TextField inputField = (TextField) getElement("tfInputFile", clickedButton.getId());
        pathsFileList.remove(Paths.get(inputField.getText()));
        inputField.clear();
        clickedButton.disableProperty().set(true);
        Button btScegliLinked = (Button) getElement("btScegli", clickedButton.getId());
        btScegliLinked.disableProperty().set(false);
    }

    @FXML
    public void findAndSave() {
        pbBar.setVisible(true);
        pbBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

        ProgressTask task = new ProgressTask(apRoot, pbBar, new HashSet<Path>(pathsFileList));

        task.setOnSucceeded(event -> {
            Map<Patient, Set<Esame>> result = task.getValue();
            if (!result.isEmpty()) {
                ControllerUtils.showAlert(Alert.AlertType.INFORMATION, "Trovati " + result.keySet().size() + " match!").showAndWait();
            }
            pbBar.setVisible(false);
        });

        task.setOnFailed(event -> {
            Throwable e = task.getException();
            ControllerUtils.showAlert(Alert.AlertType.ERROR, "Errore durante l'operazione: " + e.getMessage()).showAndWait();
            pbBar.setVisible(false);
        });

        new Thread(task).start();
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

    @Override
    public void handleException(Throwable throwable) {
        Throwable cause = getCausaErrore(throwable);
        cause.printStackTrace();
        javafxLogger.severe(cause.getMessage());
        pbBar.setVisible(false);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(cause.getMessage());
        alert.showAndWait();
    }

    private Throwable getCausaErrore(Throwable throwable) {
        Throwable cause;
        if (throwable.getCause() instanceof InvocationTargetException && throwable.getCause().getCause() != null)
            cause = throwable.getCause().getCause();
        else
            cause = throwable;
        return cause;
    }

}