package com.coworking.smartcoworking.enums;

public enum SpaceType {
    SALA_REUNIAO("Sala de Reunião"),
    MESA_FIXA("Mesa Fixa"),
    HOT_DESK("Hot Desk"),
    SALA_PRIVATIVA("Sala Privativa"),
    AUDITORIO("Auditório");

    private final String displayName;

    SpaceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}