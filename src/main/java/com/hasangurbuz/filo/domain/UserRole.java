package com.hasangurbuz.filo.domain;

public enum UserRole {
    COMPANY_ADMIN("COMPANY_ADMIN"),
    STANDARD("STANDARD");

    private String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
