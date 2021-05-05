package com.udacity.jwdnd.course1.cloudstorage.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtUserBuilder {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;

    public JwtUserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public JwtUserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public JwtUserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public JwtUserBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public JwtUserBuilder setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public JwtUserBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public JwtUser createJwtUser() {
        return new JwtUser(username, password, firstName, lastName, authorities, enabled);
    }
}