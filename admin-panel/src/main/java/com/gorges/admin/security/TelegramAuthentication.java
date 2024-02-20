package com.gorges.admin.security;

import com.gorges.admin.models.dto.WebAppInitData;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TelegramAuthentication implements Authentication {

    private final boolean authenticated;
    private final WebAppInitData webAppInitData;

    public TelegramAuthentication(boolean authenticated, WebAppInitData webAppInitData) {
        this.authenticated = authenticated;
        this.webAppInitData = webAppInitData;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return webAppInitData.initDataUnsafe.user;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        // one Authentication object per authenticated true/false?
    }

    @Override
    public String getName() {
        return null;
    }

    public WebAppInitData getWebAppInitData() {
        return webAppInitData;
    }
}
