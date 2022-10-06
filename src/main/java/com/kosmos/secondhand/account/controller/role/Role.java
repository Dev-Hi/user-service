package com.kosmos.secondhand.account.controller.role;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");
    private final String value;

    Role(String value) { this.value = value; }

    @Override
    public String getAuthority() {
        return this.value;
    }

}
