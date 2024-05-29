package it.gend.finex.parser;

import com.opencsv.CSVIterator;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import it.gend.finex.domain.AcelEsame;
import it.gend.finex.domain.Constant;
import it.gend.finex.domain.Esame;
import it.gend.finex.domain.Patient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class CsvToPatient {

    Map<String, Integer> headerMap = new HashMap<>();
    List<String> defaultHeader = List.of(Constant.defaultHeader.split(";"));
    List<String> acelHeader = List.of((Constant.defaultHeader + ";" + Constant.acelHeader).split(";"));
    Map<Patient, Set<Esame>> patientEsameMap = new HashMap<>();
    Path path;

    public CsvToPatient() {
    }
    public Map<Patient, Set<Esame>> parse(Path path) throws IOException, CsvException {
        this.path = path;
        CSVReader csvReader = getCsvReader(path);
        CSVIterator iterator = new CSVIterator(csvReader);
        if (isAcelFile(path))
            parseAcelFile(iterator);

        parseDefaultFile(iterator);
        return patientEsameMap;
    }

    private boolean isAcelFile(Path path) {
        return path.getFileName().toString().toUpperCase().contains("ACEL");
    }

    private void parseDefaultFile(CSVIterator iterator) {

        if (iterator.hasNext())
            handleHeader(iterator, defaultHeader);

        while (iterator.hasNext()) {
            List<String> line = List.of(iterator.next());
            try {
                Patient patient = mapToPatient(line);
                Esame esame = mapToExam(line);
                esame.setRisulato(line.get(headerMap.get("RISULTATO")));
                if (patientEsameMap.containsKey(patient))
                    patientEsameMap.get(patient).add(esame);
                else
                    patientEsameMap.put(patient, new LinkedHashSet<>(List.of(esame)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void parseAcelFile(CSVIterator iterator) {

        if (iterator.hasNext())
            handleHeader(iterator, acelHeader);

        while (iterator.hasNext()) {
            List<String> line = List.of(iterator.next());
            if (nonContieneValori(line))
                continue;
            try {
                Patient patient = mapToPatient(line);
                AcelEsame acelEsame = new AcelEsame(mapToExam(line));
                mapToAcelExam(line, iterator, acelEsame, patient);
                if (patientEsameMap.containsKey(patient))
                    patientEsameMap.get(patient).add(acelEsame);
                else
                    patientEsameMap.put(patient, new LinkedHashSet<>(List.of(acelEsame)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean nonContieneValori(List<String> line) {
        if (headerMap.keySet().size() > line.size())
            return true;

        if (line.stream().allMatch(String::isEmpty))
            return true;

        return line.stream().allMatch(String::isBlank);
    }

    private void mapToAcelExam(List<String> line, CSVIterator iterator, AcelEsame acelEsame, Patient patient) {
        int count = 1;
        List<List<String>> lines = new ArrayList<>();
        lines.add(0, line);
        lines.addAll(getNext5Lines(iterator, count, patient));
        for (List<String> l : lines) {
            acelEsame.addAllele(l.get(headerMap.get("ALLELENOME")),
                    l.get(headerMap.get("ALLELEVALORE")));
        }
    }

    private List<List<String>> getNext5Lines(CSVIterator iterator, int count, Patient patient) {
        List<List<String>> lines = new ArrayList<>();
        while (iterator.hasNext() && count <= 5) {
            List<String> nextLine = List.of(iterator.next());
            if (!patientIsTheSame(patient, nextLine))
                throw new RuntimeException("ID paziente inatteso esame Acel alla riga " + count + " paziente atteso: " + patient.getIdPaziente() + " : " + patient.getCognome() + " dato trovato: " + nextLine);
            lines.add(nextLine);
            count++;
        }
        return lines;
    }

    private boolean patientIsTheSame(Patient patient, List<String> line) {
        return patient.getIdPaziente().equals(line.get(headerMap.get("IDPAZIENTE")));
    }

    private Patient mapToPatient(List<String> line) {
        return new Patient(line.get(headerMap.get("IDPAZIENTE")),
                line.get(headerMap.get("COGNOME")),
                line.get(headerMap.get("NOME")),
                line.get(headerMap.get("DATANASCITA")),
                line.get(headerMap.get("SESSO")),
                path.getFileName().toString());
    }

    private Esame mapToExam(List<String> line) {
        return new Esame(line.get(headerMap.get("ANALISICODICE")),
                line.get(headerMap.get("NRICHIESTA")),
                line.get(headerMap.get("DATAPRELIEVO")),
                line.get(headerMap.get("REPARTONOME")),
                line.get(headerMap.get("REPARTOCODICE")));
    }

    private void handleHeader(CSVIterator iterator, List<String> header) {
        List<String> line = deleteBom(Stream.of(iterator.next()).map(String::toUpperCase).toList());
        for (String s : header) {
            if (line.contains(s.toUpperCase()))
                headerMap.put(s, line.indexOf(s.toUpperCase()));
        }
    }

    private List<String> deleteBom(List<String> line) {
        List<String> sanitizedLine = new ArrayList<>();
        for (String s : line) {
            if (s.contains("\uFEFF"))
                sanitizedLine.add(s.replaceAll("\uFEFF",""));
            else
                sanitizedLine.add(s);
        }
        return sanitizedLine;
    }

    private static CSVReader getCsvReader(Path path) throws IOException {
        return new CSVReaderBuilder(new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(';')
                        .withIgnoreQuotations(true)
                        .build())
                .build();
    }
}
