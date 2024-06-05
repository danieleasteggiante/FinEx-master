package it.gend.finex.domain;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Patient extends IdStrings {
    private String idPaziente;
    private String nome;
    private String cognome;
    private String dataNascita;
    private String sesso;
    private Set<String> fileName = new LinkedHashSet<>();
    private final List<Esame> esami = new ArrayList<>();

    public Patient(String idPaziente, String nome, String cognome, String dataNascita, String sesso, String fileName) {
        this.idPaziente = idPaziente;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.fileName.add(fileName);
    }

    public Patient() {
    }

    public String getIdPaziente() {
        return idPaziente;
    }

    public void setIdPaziente(String idPaziente) {
        this.idPaziente = idPaziente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public void addEsame(Esame esame){
        esami.add(esame);
    }

    public void removeEsame(Esame esame){
        esami.remove(esame);
    }

    public void addEsami(List<Esame> esami){
        this.esami.addAll(esami);
    }

    public List<Esame> getEsami(){
        return esami;
    }

    public Set<String> getFileName() {
        return fileName;
    }

    public void addFileName(String fileName) {
        this.fileName.add(fileName);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "idPaziente='" + idPaziente + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", dataNascita='" + dataNascita + '\'' +
                ", sesso='" + sesso + '\'' +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(idPaziente, patient.idPaziente);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idPaziente);
    }
}
