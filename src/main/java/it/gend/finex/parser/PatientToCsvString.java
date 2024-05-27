package it.gend.finex.parser;

import it.gend.finex.domain.Esame;
import it.gend.finex.domain.Patient;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PatientToCsvString {
    static StringBuilder sb = new StringBuilder();

    public static String generate(Map<Patient, Set<Esame>> patientEsameMap) {
        Set<String> esami = patientEsameMap.values().stream().flatMap(Set::stream).map(Esame::getAnalisiCodice).collect(Collectors.toSet());
        String header = CsvHeader.write(esami, sb);
        String body = CsvBody.write(esami, patientEsameMap);
        return header + body;
    }
}
