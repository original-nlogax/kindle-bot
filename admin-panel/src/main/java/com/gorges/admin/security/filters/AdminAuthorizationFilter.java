package com.gorges.admin.security.filters;

import com.gorges.admin.services.AdminService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AdminAuthorizationFilter extends OncePerRequestFilter  {

    private final AdminService adminService;

    public AdminAuthorizationFilter(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // fixme bc of problems with filter order we do authorization in authentication filter...
        /*
        String json = Utils.readJsonFromRequest(request);
        WebAppInitData webAppInitData = new Gson().fromJson(json, WebAppInitData.class);
        User user = webAppInitData.initDataUnsafe.user;

        if (adminService.findByChatId(user.getId()) == null)
            return;*/

        filterChain.doFilter(request, response);
    }
}
