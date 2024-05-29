package it.gend.finex.parser;

import it.gend.finex.domain.AcelEsame;
import it.gend.finex.domain.Constant;
import it.gend.finex.domain.Esame;
import it.gend.finex.domain.Patient;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CsvBody {

    private CsvBody() {
    }

    public static String write(List<String> esamiTotali, Map<Patient, Set<Esame>> patientEsameMap, StringBuilder stringBuilder) {
        if(stringBuilder == null)
            stringBuilder = new StringBuilder();
        for(Map.Entry<Patient, Set<Esame>> entry : patientEsameMap.entrySet()) {
            Patient patient = entry.getKey();
            Set<Esame> esami = entry.getValue();
            generatePazienteBody(patient, stringBuilder);
            Set<Esame> esamiRimasti = generateEsamiBody(stringBuilder, esamiTotali, esami, patient);
            if(!esamiRimasti.isEmpty()) {
                stringBuilder.append("\n");
                write(List.copyOf(esamiTotali), Map.of(patient, esamiRimasti), stringBuilder);
                continue;
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private static void generatePazienteBody(Patient patient, StringBuilder stringBuilder) {
        stringBuilder.append(patient.getIdPaziente()).append(";")
                .append(patient.getNome()).append(";")
                .append(patient.getCognome()).append(";")
                .append(patient.getDataNascita()).append(";")
                .append(patient.getSesso()).append(";");
    }

    private static Set<Esame> generateEsamiBody(StringBuilder stringBuilder, List<String> esamiTotali, Set<Esame> esamiPaziente, Patient patient) {
        Iterator<String> iterator = esamiTotali.iterator();
        Set<String> esamiGiaScritti = new HashSet<>();
        while (iterator.hasNext()) {
            String codiceEsame = iterator.next();
            Esame esamePaziente = getEsameByCodice(esamiPaziente, codiceEsame);
            if (esamePaziente == null) {
                writeEmptyEsame(esamiTotali, codiceEsame, stringBuilder);
                continue;
            }
            if (esamePuoStareInQuestaRiga(esamePaziente, esamiTotali, esamiGiaScritti)) {
                handleEsame(esamePaziente, stringBuilder);
                esamiGiaScritti.add(codiceEsame);
                esamiPaziente.remove(esamePaziente);
            }
        }
        return esamiPaziente;
    }

    private static Esame getEsameByCodice(Set<Esame> esamiPaziente, String codiceEsame) {
        for(Esame esame : esamiPaziente) {
            if(esame.getAnalisiCodice().equals(codiceEsame))
                return esame;
        }
        return null;
    }

    private static void writeEmptyEsame(List<String> esamiTotali, String esameAttuale, StringBuilder stringBuilder) {
        int posizioneEsame = esamiTotali.indexOf(esameAttuale);
        int spaziDaRiempire = esamiTotali.size() - posizioneEsame;
        if(spaziDaRiempire == 0)
            return;
        if (esameAttuale.equalsIgnoreCase("ACEL")) {
            stringBuilder.append("-;".repeat(Constant.acelHeaderSize));
            return;
        }
        stringBuilder.append("-;".repeat(Constant.esameHeaderSize));
    }

    private static boolean esamePuoStareInQuestaRiga(Esame esame, List<String> esamiTotali, Set<String> esamiGiaScritti) {
        return esamiTotali.contains(esame.getAnalisiCodice()) && !esamiGiaScritti.contains(esame.getAnalisiCodice());
    }

    private static void handleEsame(Esame esame, StringBuilder stringBuilder) {
        if (esame instanceof AcelEsame) {
            writeAcelEsame((AcelEsame) esame, stringBuilder);
            return;
        }
        writeEsame(esame, stringBuilder);
    }


    private static void writeAcelEsame(AcelEsame esameSet, StringBuilder body) {
        body.append("ACEL").append(";")
                .append(esameSet.getAllele("DQA1_1")).append(";")
                .append(esameSet.getAllele("DQA1_2")).append(";")
                .append(esameSet.getAllele("DQB1_1")).append(";")
                .append(esameSet.getAllele("DQB1_2")).append(";")
                .append(esameSet.getAllele("DRB1_1")).append(";")
                .append(esameSet.getAllele("DRB1_2")).append(";")
                .append(esameSet.getRisulato()).append(";");
    }

    private static void writeEsame(Esame esameSet, StringBuilder body) {
        body.append(esameSet.getAnalisiCodice()).append(";")
                .append(esameSet.getnRichiesta()).append(";")
                .append(esameSet.getDataPrelievo()).append(";")
                .append(esameSet.getRepartoNome()).append(";")
                .append(esameSet.getRepartoCodice()).append(";")
                .append(esameSet.getRisulato()).append(";");
    }
}
