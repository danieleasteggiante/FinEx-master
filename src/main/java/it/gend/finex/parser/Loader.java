package it.gend.finex.parser;

import it.gend.finex.domain.Esame;
import it.gend.finex.domain.Patient;
import javafx.scene.control.ProgressBar;

import java.nio.file.Path;
import java.util.*;

public class Loader {

    private final Set<String> paths;

    private ProgressBar pbBar;
    private Loader(Set<String> paths) {
        this.paths = paths;
    }

    public static Loader of(Set<String> paths) {
        return new Loader(paths);
    }

    public Map<Patient, Set<Esame>> load() throws Throwable {
        Map<Patient, Set<Esame>> result = new HashMap<>();
        for (String path : paths) {
            if (!path.endsWith(".csv")) {
                throw new IllegalArgumentException("Il file " + path + " non è un file CSV");
            }
            CsvToPatient csvToPatient = new CsvToPatient();
            mergeMaps(result, csvToPatient.parse(Path.of(path)), path);
        }
        removeNonMatchingElements(result);
        return sortByCognome(result);
    }

    private void mergeMaps(Map<Patient, Set<Esame>> result, Map<Patient, Set<Esame>> newMap, String path) {
        for (Map.Entry<Patient, Set<Esame>> entry : newMap.entrySet()) {
            Patient patient = entry.getKey();
            addFileName(patient, result, path);
            Set<Esame> esami = entry.getValue();
            if (result.containsKey(patient))
                result.get(patient).addAll(esami);
            else
                result.put(patient, esami);
        }
    }

    private void addFileName(Patient patient, Map<Patient, Set<Esame>> result, String path) {
        if (!result.containsKey(patient))
            return;
        Patient resultPatient = getPatient(result, patient);
        resultPatient.addFileName(path);
    }

    private Patient getPatient(Map<Patient, Set<Esame>> result, Patient patient) {
        for (Patient p : result.keySet()) {
            if (p.equals(patient))
                return p;
        }
        throw new IllegalArgumentException("Il paziente " + patient + " non è presente nella mappa");
    }

    private Map<Patient, Set<Esame>> sortByCognome(Map<Patient, Set<Esame>> result) {
        Map<Patient, Set<Esame>> treeMap = new TreeMap<>(Comparator.comparing(Patient::getCognome));
        treeMap.putAll(result);
        return treeMap;
    }

    private void removeNonMatchingElements(Map<Patient, Set<Esame>> result) {
        result.entrySet().removeIf(entry -> entry.getKey().getFileName().size() < 2);
    }
}
