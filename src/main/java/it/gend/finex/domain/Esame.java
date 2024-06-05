package it.gend.finex.domain;

import java.util.Objects;

public class Esame extends IdStrings{
    private String idEsame;
    private String analisiCodice;
    private String nRichiesta;
    private String dataPrelievo;
    private String repartoNome;
    private String repartoCodice;
    private String risulato;



    public Esame(String analisiCodice, String nRichiesta, String dataPrelievo, String repartoNome, String repartoCodice) {
        this.analisiCodice = analisiCodice;
        this.nRichiesta = addZeroesIfNeeded(nRichiesta);
        this.dataPrelievo = dataPrelievo;
        this.repartoNome = repartoNome;
        this.repartoCodice = repartoCodice;
        this.idEsame = analisiCodice + nRichiesta;
    }

    public String getAnalisiCodice() {
        return analisiCodice;
    }

    public void setAnalisiCodice(String analisiCodice) {
        this.analisiCodice = analisiCodice;
    }

    public String getnRichiesta() {
        return nRichiesta;
    }

    public void setnRichiesta(String nRichiesta) {
        this.nRichiesta = nRichiesta;
    }

    public String getDataPrelievo() {
        return dataPrelievo;
    }

    public void setDataPrelievo(String dataPrelievo) {
        this.dataPrelievo = dataPrelievo;
    }

    public String getRepartoNome() {
        return repartoNome;
    }

    public void setRepartoNome(String repartoNome) {
        this.repartoNome = repartoNome;
    }

    public String getRepartoCodice() {
        return repartoCodice;
    }

    public void setRepartoCodice(String repartoCodice) {
        this.repartoCodice = repartoCodice;
    }

    public String getRisulato() {
        return risulato;
    }

    public void setRisulato(String risulato) {
        this.risulato = risulato;
    }

    public String getIdEsame() {
        return idEsame;
    }

    @Override
    public String toString() {
        return "Esame{" +
                "idEsame='" + idEsame + '\'' +
                ", analisiCodice='" + analisiCodice + '\'' +
                ", nRichiesta='" + nRichiesta + '\'' +
                ", dataPrelievo='" + dataPrelievo + '\'' +
                ", repartoNome='" + repartoNome + '\'' +
                ", repartoCodice='" + repartoCodice + '\'' +
                ", risulato='" + risulato + '\'' +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Esame esame)) return false;
        return Objects.equals(getIdEsame(), esame.getIdEsame()) && Objects.equals(getAnalisiCodice(), esame.getAnalisiCodice()) && Objects.equals(getnRichiesta(), esame.getnRichiesta());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdEsame(), getAnalisiCodice(), getnRichiesta());
    }
}
