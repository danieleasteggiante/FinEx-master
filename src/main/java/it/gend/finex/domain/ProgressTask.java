package it.gend.finex.domain;

/**
 * @author Daniele Asteggiante
 */

import it.gend.finex.parser.CsvExporter;
import it.gend.finex.parser.Loader;
import it.gend.finex.parser.PatientToCsvString;
import it.gend.finex.utils.ControllerUtils;
import javafx.concurrent.Task;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProgressTask extends Task<Map<Patient, Set<Esame>>> {

    private final AnchorPane apRoot;
    private final ProgressBar pbBar;
    private final Set<Path> pathsFileList;

    public ProgressTask(AnchorPane apRoot, ProgressBar pbBar, Set<Path> pathsFileList) {
        this.apRoot = apRoot;
        this.pbBar = pbBar;
        this.pathsFileList = pathsFileList;
    }

    @Override
    protected Map<Patient, Set<Esame>> call() throws Exception {
        pbBar.setVisible(true);
        Map<Patient, Set<Esame>> result = getResults();
        if(result.isEmpty())
            return result;
        saveFile(result);
        return result;
    }

    private void saveFile(Map<Patient, Set<Esame>> result) {
        Platform.runLater(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
            File output = fileChooser.showSaveDialog(apRoot.getScene().getWindow());
            if (output != null) {
                try {
                    generateCsv(result, output);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            pbBar.setVisible(false);
        });
    }

    private Map<Patient, Set<Esame>> getResults() {
        Map<Patient, Set<Esame>> result;
        try {
            result = Loader.of(pathsFileList.stream().map(Path::toString)
                    .collect(Collectors.toSet())).load();
            ;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        if (result.isEmpty()) {
            Platform.runLater(() -> {
                ControllerUtils.showAlert(Alert.AlertType.INFORMATION, "Nessun risultato trovato").showAndWait();
                pbBar.setVisible(false);
            });
        }
        return result;
    }


    private void generateCsv(Map<Patient, Set<Esame>> result, File output) throws IOException {
        String outputString = PatientToCsvString.generate(result);
        CsvExporter.writeCSV(outputString, output);
        ControllerUtils.showAlert(Alert.AlertType.INFORMATION, "File salvato correttamente").showAndWait();
    }


}
