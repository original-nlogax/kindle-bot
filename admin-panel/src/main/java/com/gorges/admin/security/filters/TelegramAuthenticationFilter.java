package com.gorges.admin.security.filters;

import com.google.gson.Gson;
import com.gorges.admin.configs.SpringConfig;
import com.gorges.admin.models.dto.User;
import com.gorges.admin.models.dto.WebAppInitData;
import com.gorges.admin.security.TelegramAuthentication;
import com.gorges.admin.services.AdminService;
import com.gorges.admin.utils.Utils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TelegramAuthenticationFilter extends OncePerRequestFilter  {

    private final AuthenticationManager manager;
    private final AdminService adminService;

    public TelegramAuthenticationFilter(AuthenticationManager manager, AdminService adminService) {
        this.manager = manager;
        this.adminService = adminService;
    }

    // fixme bc of problems with filter order we do authorization in authentication filter...
    private void authorize(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        User user = (User)authentication.getPrincipal();

        if (adminService.findByChatId(user.getId()) == null) {
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication maybeAuthentication = securityContext.getAuthentication();

        if (maybeAuthentication != null && maybeAuthentication.isAuthenticated()) {
            authorize(request, response, filterChain);
            return;
        }

        String json = Utils.readJsonFromRequest(request);
        if (json == null || json.isBlank()) {
            System.out.println("No JSON is sent and Authentication is null");
            response.setStatus(400);
            return;
        }

        WebAppInitData webAppInitData = new Gson().fromJson(json, WebAppInitData.class);
        if (webAppInitData.initData.isBlank() || webAppInitData.initDataUnsafe == null) {
            System.out.println("initData or initDataUnsafe is null and Authentication is null");
            response.setStatus(400);
            return;
        }

        TelegramAuthentication authentication = new TelegramAuthentication(false, webAppInitData);

        // eventually gets to the provider and returns authenticated Authentication
        Authentication authenticatedObject = manager.authenticate(authentication);
        securityContext.setAuthentication(authenticatedObject);

        // Create new session and set the security context cookie
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        authorize(request, response, filterChain);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return (SpringConfig.get("server.servlet.context-path") + "/").equals(path);
    }
}
