package com.coworking.smartcoworking.enums;

public enum ReservationStatus {
    PENDENTE("Pendente"),
    CONFIRMADA("Confirmada"),
    EM_USO("Em Uso"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada"),
    EXPIRADA("Expirada");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}