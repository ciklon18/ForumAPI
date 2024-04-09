package com.common.auth.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;
import java.util.UUID;

public class JwtAuthentication extends AbstractAuthenticationToken {
    public JwtAuthentication(UUID id, List<Role> roles, boolean authenticated) {
        super(roles);
        this.setAuthenticated(authenticated);
        this.setDetails(id);
    }

    public JwtAuthentication(boolean authenticated) {
        super(null);
        this.setAuthenticated(authenticated);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return getDetails();
    }
}
