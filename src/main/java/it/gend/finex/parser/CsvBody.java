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

    // TODO gestire il caso in cui ci siano due esami con lo
    //      stesso codice deve andare a capo riempiendo tutto cio che non ha fatto coi trattini
    //      calcolare il numero di trattini che deve inserire
    public static String write(Set<String> esamiTotaliSet, Map<Patient, Set<Esame>> patientEsameMap) {
        StringBuilder bodySb = new StringBuilder();
        List<String> esamiTotali = List.copyOf(esamiTotaliSet);
        patientEsameMap.forEach((patient, esami) -> {
            bodySb.append(patient.getIdPaziente()).append(";")
                    .append(patient.getNome()).append(";")
                    .append(patient.getCognome()).append(";")
                    .append(patient.getDataNascita()).append(";")
                    .append(patient.getSesso()).append(";");
            Set<Esame> esamiRimasti = generateEsamiBody(bodySb, esamiTotali, esami, patient);
            if(!esamiRimasti.isEmpty()) {
                bodySb.append("\n");
                write(Set.copyOf(esamiTotali), Map.of(patient, esamiRimasti));
            }
            bodySb.append("\n");
        });
        return bodySb.toString();
    }

    private static Set<Esame> generateEsamiBody(StringBuilder bodySb, List<String> esamiTotali, Set<Esame> esami, Patient patient) {
        Iterator<Esame> iterator = esami.iterator();
        Set<String> esamiGiaScritti = new HashSet<>();
        while (iterator.hasNext()) {
            Esame esame = iterator.next();
            if (esamePuoStareInQuestaRiga(esame, esamiTotali, esamiGiaScritti)) {
                handleEsame(esame, bodySb);
                esamiGiaScritti.add(esame.getAnalisiCodice());
                iterator.remove();
            }
            writeEmptyEsame(esamiTotali, esame, bodySb);
        }
        return esami;
    }

    private static void writeEmptyEsame(List<String> esamiTotali, Esame esame, StringBuilder bodySb) {
        int posizioneEsame = esamiTotali.indexOf(esame.getAnalisiCodice());
        int spaziDaRiempire = esamiTotali.size() - posizioneEsame;
        if(spaziDaRiempire == 0)
            return;
        if (esame instanceof AcelEsame) {
            bodySb.append("-;".repeat(Constant.acelHeaderSize));
            return;
        }
        bodySb.append("-;".repeat(Constant.esameHeaderSize));
    }

    private static boolean esamePuoStareInQuestaRiga(Esame esame, List<String> esamiTotali, Set<String> esamiGiaScritti) {
        return esamiTotali.contains(esame.getAnalisiCodice()) && !esamiGiaScritti.contains(esame.getAnalisiCodice());
    }

    private static void handleEsame(Esame esame, StringBuilder bodySb) {
        if (esame instanceof AcelEsame) {
            writeAcelEsame((AcelEsame) esame, bodySb);
            return;
        }
        writeEsame(esame, bodySb);
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
