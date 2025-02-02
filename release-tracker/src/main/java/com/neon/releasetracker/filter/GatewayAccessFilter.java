package com.neon.releasetracker.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/**")
public class GatewayAccessFilter extends OncePerRequestFilter {
    private static final String GATEWAY_HOST = "http://localhost:8081";
    private static final String SWAGGER_UI = "swagger-ui";
    @Value("${release-tracker.enable.gateway}")
    private boolean enableGateway;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(enableGateway) {
            var referer = request.getHeader("Referer");
            var origin = request.getHeader("Origin");
            if ((referer != null && referer.startsWith(GATEWAY_HOST)) ||
                    (origin != null && origin.startsWith(GATEWAY_HOST)) ||
                    (referer != null && referer.contains(SWAGGER_UI)) ||
                    request.getRequestURL().toString().contains(SWAGGER_UI)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(403);
                response.getWriter().write("Access denied. This endpoint is only accessible through the Release Tracker gateway.");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
