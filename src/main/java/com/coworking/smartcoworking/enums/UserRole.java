package com.coworking.smartcoworking.enums;

public enum UserRole {
    ADMIN("Administrador"),
    MEMBER("Membro"),
    VISITOR("Visitante");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}