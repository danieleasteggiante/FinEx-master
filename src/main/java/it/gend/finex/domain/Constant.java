package it.gend.finex.domain;

public interface Constant {
    String pazienteHeader = "IDPAZIENTE;COGNOME;NOME;DATANASCITA;SESSO;";
    String esameHeader = "ANALISICODICE;NRICHIESTA;DATAPRELIEVO;REPARTONOME;REPARTOCODICE";
    String acelHeader = "ALLELENOME;ALLELEVALORE;DQA1_1;DQA1_2;DQB1_1;DQB1_2;DRB1_1;DRB1_2";
    String risulatoHeader = "RISULTATO";
    String acelHeaderPrint = "ANALISICODICE;DQA1_1;DQA1_2;DQB1_1;DQB1_2;DRB1_1;DRB1_2;" + risulatoHeader;
    String defaultHeader = pazienteHeader + esameHeader + ";" +risulatoHeader;
    int acelHeaderSize = 8;
    int esameHeaderSize = 6;
}
