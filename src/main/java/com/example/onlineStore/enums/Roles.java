package com.example.onlineStore.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Roles {
    ADMIN(Set.of(Permission.ADMIN_READ,Permission.ADMIN_UPDATE)),
    USER(Set.of(Permission.ADMIN_READ));

    private final Set<Permission> permissions;
//    ADMIN,
//    USER

    Roles(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
