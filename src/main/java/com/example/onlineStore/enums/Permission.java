package com.example.onlineStore.enums;

public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
