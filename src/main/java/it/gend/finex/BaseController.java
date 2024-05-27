package it.gend.finex;

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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseController extends LoggerClass {
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

    /**
     * fase1 : caricamento files - OK
     * fase2 : se il fileName contiene "ACEL" gestire il caso*
     * fase3 : inserire 0 davanti a numeri se inferiore a 8 cifre - OK
     * fase4 : unire tutti i files in un solo elenco- OK
     * fase5 : ordinare l'elenco
     * fase6 : definire la struttura della tabella di output
     * fase7 : raggruppare i record per id paziente in struttura dati che permetta le ripetizioni
     * fase8 : per ogni gruppo di record, creare una riga di tabella
     * fase9 : se ci sono doppioni di esame per lo stesso paziente, creare una riga di tabella aggiuntiva per quel paziente
     * fase10 : se alcuni campi sono vuoti, inserire un valore di default
     * fase11 : scrivere la tabella su file
     * fase12 : fine
     * <p>
     * *caso ACEL :
     * - controllare che le righe con nome dei pazienti siano 6 altrimenti scartare loggando l'errore in un file - OK
     * - per ogni riga valorizzare la colonna con il nome del campo sotto la colonna AlleleHLA con il valore della colonna valoreAllele
     * - raggruppare i record in un unico record per paziente
     * - se uno degi alleli, con nome che inizia per DQ, è valorizzato
     * - inserire nella nuova colonna risultato il valore=POSITIVO - OK
     */

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
    void findAndSave(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File output = fileChooser.showSaveDialog(apRoot.getScene().getWindow());
        javafxLogger.info("findAndSave");
        javafxLogger.info(pathsFileList.toString());
        Map<Patient, Set<Esame>> result = Loader.of(pathsFileList.stream().map(Path::toString)
                .collect(Collectors.toSet())).load();
        String csvString = PatientToCsvString.generate(result);
        CsvExporter.writeCSV(csvString,output);
    }

}