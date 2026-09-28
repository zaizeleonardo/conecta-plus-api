package com.conecta.conect.plus.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        // Se não houver token, continua normalmente.
        if (authorizationHeader == null ||
            !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authorizationHeader.substring(7);

        try {

            Claims claims =
                    jwtService.extrairClaims(token);

            String usuarioId =
                    claims.getSubject();

            String perfil =
                    claims.get("perfil", String.class);

            /*
             * Define a autoridade do usuário.
             *
             * ADMIN   -> ROLE_ADMIN
             * USUARIO -> ROLE_USUARIO
             */

            String autoridade =
                    "ROLE_" + perfil;

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            usuarioId,
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(
                                            autoridade
                                    )
                            )
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            // DIAGNÓSTICO TEMPORÁRIO
            System.out.println(
                    ">>> JWT: "
                    + request.getMethod()
                    + " "
                    + request.getRequestURI()
                    + " | PERFIL: "
                    + perfil
                    + " | AUTORIDADE: "
                    + autoridade
            );

        } catch (Exception erro) {

            System.out.println(
                    ">>> ERRO JWT: "
                    + erro.getClass().getName()
            );

            System.out.println(
                    ">>> MENSAGEM JWT: "
                    + erro.getMessage()
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(null);

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }

        /*
         * Depois de validar o JWT,
         * continua a cadeia de filtros.
         */

        filterChain.doFilter(request, response);
    }
}