package it.gend.finex.parser;

import it.gend.finex.domain.Esame;
import it.gend.finex.domain.Patient;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PatientToCsvString {
    public static String generate(Map<Patient, Set<Esame>> patientEsameMap) {
        Set<String> esami = patientEsameMap.values().stream().flatMap(Set::stream).map(Esame::getAnalisiCodice).collect(Collectors.toSet());
        if(esami.isEmpty())
            return "";
        String header = CsvHeader.write(esami);
        String body = CsvBody.write(List.copyOf(esami), patientEsameMap, null);
        return header + body;
    }
}
