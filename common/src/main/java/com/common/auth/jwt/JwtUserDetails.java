package com.common.auth.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
public class JwtUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean isAuthenticated;
    private UUID id;
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAuthenticated;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAuthenticated;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isAuthenticated;
    }

    @Override
    public boolean isEnabled() {
        return isAuthenticated;
    }
}
