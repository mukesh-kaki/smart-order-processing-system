package com.mukesh.order.filter;

import com.mukesh.order.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtClaimsAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        String username = jwtService.extractUsername(token);

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            List<String> roles = jwtService.extractRoles(token);

            List<String> permissions =
                    jwtService.extractPermissions(token);

            List<SimpleGrantedAuthority> authorities =

                    Stream.concat(

                                    roles.stream(),

                                    permissions.stream()

                            )

                            .distinct()

                            .map(SimpleGrantedAuthority::new)

                            .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =

                    new UsernamePasswordAuthenticationToken(

                            username,

                            null,

                            authorities
                    );

            authentication.setDetails(

                    new WebAuthenticationDetailsSource()

                            .buildDetails(request)

            );

            SecurityContextHolder

                    .getContext()

                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}