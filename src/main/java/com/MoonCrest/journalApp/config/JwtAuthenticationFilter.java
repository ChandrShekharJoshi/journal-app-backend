package com.MoonCrest.journalApp.config;

import com.MoonCrest.journalApp.service.CustomUserDetailsService;
import com.MoonCrest.journalApp.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader =
                request.getHeader("Authorization");

        /*
         * Check whether Authorization header exists
         * and starts with "Bearer "
         */
        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        /*
         * Remove "Bearer " from Authorization header
         */
        final String jwt =
                authHeader.substring(7);

        try {

            /*
             * Extract username from JWT
             */
            String username =
                    jwtService.extractUsername(jwt);

            System.out.println(
                    "JWT USERNAME: " + username
            );

            /*
             * Check whether user is not already authenticated
             */
            if (username != null
                    && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                /*
                 * Load user from MongoDB
                 */
                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(username);

                System.out.println(
                        "USER FROM DATABASE: "
                                + userDetails.getUsername()
                );

                /*
                 * Print user's authorities/roles
                 */
                System.out.println(
                        "USER AUTHORITIES: "
                                + userDetails.getAuthorities()
                );

                /*
                 * Validate JWT
                 */
                boolean validToken =
                        jwtService.isTokenValid(
                                jwt,
                                userDetails
                        );

                System.out.println(
                        "JWT VALID: " + validToken
                );

                if (validToken) {

                    /*
                     * Create authenticated user
                     */
                    UsernamePasswordAuthenticationToken
                            authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    /*
                     * Add request details
                     */
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    /*
                     * Store authentication in SecurityContext
                     */
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    System.out.println(
                            "AUTHENTICATION SET SUCCESSFULLY"
                    );
                }
            }

        } catch (Exception exception) {

            /*
             * Temporary debugging.
             *
             * This will show the actual JWT/security
             * exception in the console.
             */
            System.out.println(
                    "JWT AUTHENTICATION FAILED"
            );

            exception.printStackTrace();
        }

        /*
         * Continue request
         */
        filterChain.doFilter(request, response);
    }
}

