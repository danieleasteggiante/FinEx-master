package it.gend.finex.parser;

import com.opencsv.exceptions.CsvException;
import it.gend.finex.LoggerClass;
import it.gend.finex.domain.Esame;
import it.gend.finex.domain.Patient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Loader extends LoggerClass {

    private final Set<String> paths;

    private Loader(Set<String> paths) {
        this.paths = paths;
    }

    public static Loader of(Set<String> paths) {
        return new Loader(paths);
    }

    public Map<Patient, Set<Esame>> load() {
        return paths.stream()
                .map(filePath -> {
                    CsvToPatient csvToPatient = new CsvToPatient();
                    try {
                        return csvToPatient.parse(Path.of(filePath));
                    } catch (IOException | CsvException e) {
                        javafxLogger.severe(e.getMessage());
                        throw new RuntimeException("Errore durante il parsing del file " + filePath, e);
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> entry.getKey().getFileName().size() < 2)
                .sorted(Comparator.comparing(entry -> entry.getKey().getCognome()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new HashSet<>(entry.getValue()),
                        (existing, replacement) -> {
                            existing.addAll(replacement);
                            return existing;
                        }
                ));
    }
}
