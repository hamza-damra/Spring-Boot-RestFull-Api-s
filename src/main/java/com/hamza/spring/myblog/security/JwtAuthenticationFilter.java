package com.hamza.spring.myblog.security;

import com.hamza.spring.myblog.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    private final List<String> unsecuredEndpoints = List.of(
            "/api/users/**", "/api/roles/**"
    );

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (isUnsecuredEndpoint(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = getJwtTokenFromRequest(request);
            logger.info("JWT token from request: " + token);

            if (!StringUtils.hasText(token)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Token is required. Please add a token.");
                return;
            }

            if (jwtTokenProvider.validateToken(token)) {
                String userName = jwtTokenProvider.getUserNameFromToken(token);
                logger.info("Username from token: " + userName);

                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("User authenticated: " + userName);
            }

            filterChain.doFilter(request, response);

        } catch (JwtAuthenticationException ex) {
            SecurityContextHolder.clearContext();
            logger.warning("JWT authentication failed: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid JWT token. Please log in again.");
        }
    }

    private boolean isUnsecuredEndpoint(String requestURI) {
        return unsecuredEndpoints.stream().anyMatch(uri -> requestURI.startsWith(uri.replace("**", "")));
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
