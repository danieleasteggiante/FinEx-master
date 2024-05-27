package it.gend.finex.parser;

import it.gend.finex.domain.AcelEsame;
import it.gend.finex.domain.Constant;
import it.gend.finex.domain.Esame;

import java.util.Collection;
import java.util.Set;

public class CsvHeader {
    boolean isPresentAcel = false;

    private CsvHeader() {
    }

    public static String write(Set<String> esami, StringBuilder header) {
        CsvHeader csvHeader = new CsvHeader();
        header.append(Constant.pazienteHeader);
        esami.forEach(esame -> {
                    if (!header.toString().endsWith(";"))
                        header.append(";");
                    if (esame.equalsIgnoreCase("ACEL") && !csvHeader.isPresentAcel) {
                        csvHeader.isPresentAcel = true;
                        header.append(Constant.acelHeaderPrint);
                        return;
                    }
                    header.append(Constant.esameHeader).append(";")
                            .append(Constant.risulatoHeader);
                }
        );
        header.append("\n");
        return header.toString();
    }

}
