package it.gend.finex.domain;

public class IdStrings {
    protected String addZeroesIfNeeded(String id) {
        if (id.length() < 6) {
            int zeroesToAdd = 6 - id.length();
            return "0".repeat(zeroesToAdd) + id;
        }
        return id;
    }
}
