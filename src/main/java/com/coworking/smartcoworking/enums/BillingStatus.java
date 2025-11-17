package com.coworking.smartcoworking.enums;

public enum BillingStatus {
    PENDENTE("Pendente"),
    PAGA("Paga"),
    ATRASADA("Atrasada"),
    CANCELADA("Cancelada"),
    REEMBOLSADA("Reembolsada");

    private final String displayName;

    BillingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}