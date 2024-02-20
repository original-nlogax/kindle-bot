
package com.gorges.admin.configs;

import com.gorges.admin.security.filters.TelegramAuthenticationFilter;
import com.gorges.admin.security.managers.TelegramAuthenticationManager;
import com.gorges.admin.security.providers.TelegramAuthenticationProvider;
import com.gorges.admin.services.AdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final AdminService adminService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationProvider authenticationProvider;

    public WebSecurityConfig(AdminService adminService) {
        this.adminService = adminService;
        authenticationProvider = new TelegramAuthenticationProvider();
        authenticationManager = new TelegramAuthenticationManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /*
            Почему фильтры не бины:
            --> https://stackoverflow.com/questions/74386736
         */
        TelegramAuthenticationFilter authenticationFilter = new TelegramAuthenticationFilter(authenticationManager, adminService);
        //AdminAuthorizationFilter authorizationFilter = new AdminAuthorizationFilter(adminService);

        http
            //.authenticationProvider(provider)
            //.authenticationManager(manager)
            //.addFilterAfter(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .anonymous().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .x509().disable()
            .jee().disable()
            .cors().disable()
            .csrf().disable()
            .logout().disable()
            .rememberMe().disable()
            .authorizeHttpRequests().requestMatchers("/").permitAll()
            .and()
            .authorizeHttpRequests().anyRequest().authenticated();

        //pass-through
        //http
        //    .cors().disable()
        //    .csrf().disable()
        //    .authorizeHttpRequests().anyRequest().permitAll();

        return http.build();
    }
}
