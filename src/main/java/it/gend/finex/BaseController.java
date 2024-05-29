package it.gend.finex;

import com.opencsv.exceptions.CsvException;
import it.gend.finex.domain.Esame;
import it.gend.finex.domain.Patient;
import it.gend.finex.parser.CsvExporter;
import it.gend.finex.parser.Loader;
import it.gend.finex.parser.PatientToCsvString;
import it.gend.finex.utils.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static it.gend.finex.LoggerClass.javafxLogger;

public class BaseController {
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
            ((Button)getElement("btClear",button.getId())).setDisable(false);
        }
    }


    @FXML
    void searchAndAddFile(MouseEvent event) {
        Button clickedButton = (Button) event.getSource();
        try {
            openFileAndAddToList(clickedButton);
        } catch (NoSuchFieldException e) {
            javafxLogger.severe("Button not found: " + e.getMessage());
        } catch (IllegalAccessException e) {
            javafxLogger.severe("Illegal access: " + e.getMessage());
        }
    }

    @FXML
    void clearFileAndDisable(MouseEvent event) {
        Button clickedButton = (Button) event.getSource();
        try {
            TextField inputField = (TextField) getElement("tfInputFile",clickedButton.getId());
            pathsFileList.remove(Paths.get(inputField.getText()));
            inputField.clear();
            clickedButton.disableProperty().set(true);
            Button btScegliLinked = (Button) getElement("btScegli",clickedButton.getId());
            btScegliLinked.disableProperty().set(false);
        } catch (NoSuchFieldException e) {
            javafxLogger.severe("Field not found: " + e.getMessage());
        } catch (IllegalAccessException e) {
            javafxLogger.severe("Illegal access: " + e.getMessage());
        }
    }

    @FXML
    void findAndSave() throws IOException, CsvException {
        Map<Patient, Set<Esame>> result = getResults();
        if(result.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Nessun risultato trovato").showAndWait();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File output = fileChooser.showSaveDialog(apRoot.getScene().getWindow());
        generateCsv(result, output);
    }

    private void generateCsv(Map<Patient, Set<Esame>> result, File output) {
        try {
            String outputString = PatientToCsvString.generate(result);
            CsvExporter.writeCSV(outputString,output);
            showAlert(Alert.AlertType.INFORMATION,"File salvato correttamente").showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,"Errore durante il salvataggio del file " + e.getMessage()).showAndWait();
            throw new RuntimeException(e);
        }
    }

    private Map<Patient, Set<Esame>> getResults() throws IOException, CsvException {
        return Loader.of(pathsFileList.stream().map(Path::toString)
                    .collect(Collectors.toSet())).load();
    }

    private Alert showAlert(Alert.AlertType alertType, String message){
        Alert a = new Alert(alertType);
        a.setContentText(message);
        return a;
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