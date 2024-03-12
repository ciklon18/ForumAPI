package com.user.config.authorization;

import org.springframework.security.core.GrantedAuthority;

public record Authority(String authority) implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return authority;
    }
}
