package it.gend.finex.domain;

public interface Constant {
    String pazienteHeader = "IDPAZIENTE;NOME;COGNOME;DATANASCITA;SESSO;";
    String esameHeader = "ANALISICODICE;NRICHIESTA;DATAPRELIEVO;REPARTONOME;REPARTOCODICE";
    String acelHeader = "NRICHIESTA;ALLELENOME;ALLELEVALORE;DQA1_1;DQA1_2;DQB1_1;DQB1_2;DRB1_1;DRB1_2";
    String risulatoHeader = "RISULTATO";
    String acelHeaderPrint = "ANALISICODICE;NRICHIESTA;DQA1_1;DQA1_2;DQB1_1;DQB1_2;DRB1_1;DRB1_2;" + risulatoHeader;
    String defaultHeader = pazienteHeader + esameHeader + ";" +risulatoHeader;
    int acelHeaderSize = 9;
    int esameHeaderSize = 6;
}
