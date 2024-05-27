package it.gend.finex.domain;

import java.util.HashMap;
import java.util.Map;

public class AcelEsame extends Esame {
    Map<String,String> alleleMap = new HashMap<>();

    public AcelEsame(String analisiCodice, String nRichiesta, String dataPrelievo, String repartoNome, String repartoCodice) {
        super(analisiCodice, nRichiesta, dataPrelievo, repartoNome, repartoCodice);
    }

    public AcelEsame(Esame esame) {
        super(esame.getAnalisiCodice(), esame.getnRichiesta(), esame.getDataPrelievo(), esame.getRepartoNome(), esame.getRepartoCodice());
    }

    public void addAllele(String alleleName, String alleleValue) {
        alleleMap.put(alleleName, alleleValue);
    }

    public String getAllele(String alleleName) {
        return alleleMap.get(alleleName) != null ? alleleMap.get(alleleName) : " - ";
    }

    public Map<String, String> getAlleleMap() {
        return alleleMap;
    }

    public void setAlleleMap(Map<String, String> alleleMap) {
        this.alleleMap = alleleMap;
    }

    @Override
    public void setRisulato(String risulato) {
        boolean positive = alleleMap.entrySet().stream()
                .anyMatch(entry -> entry.getKey().toUpperCase().startsWith("DQ") && !entry.getValue().isEmpty());
        super.setRisulato(positive ? "POSITIVO" : "NEGATIVO");
    }

    @Override
    public String toString() {
        return "AcelEsame{" +
                "alleleMap=" + alleleMap +
                '}';
    }
}
