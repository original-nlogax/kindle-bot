package com.gorges.admin.security.providers;

import com.gorges.admin.configs.SpringConfig;
import com.gorges.admin.models.dto.WebAppInitData;
import com.gorges.admin.security.TelegramAuthentication;
import com.gorges.admin.utils.TelegramWebappDataValidatorUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class TelegramAuthenticationProvider implements AuthenticationProvider {

    private final String BOT_TOKEN;

    public TelegramAuthenticationProvider () {
        BOT_TOKEN = SpringConfig.get("telegram.bot.token");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WebAppInitData webAppInitData = ((TelegramAuthentication) authentication).getWebAppInitData();

        if (TelegramWebappDataValidatorUtil.isValid(webAppInitData.initData, BOT_TOKEN)) {
            return new TelegramAuthentication(true, webAppInitData);
        }

        throw new BadCredentialsException("WebApp hash or bot token is invalid");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TelegramAuthentication.class.equals(authentication);
    }
}
