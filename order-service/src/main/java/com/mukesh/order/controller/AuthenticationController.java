package com.mukesh.order.controller;

import com.mukesh.order.audit.service.AuditService;
import com.mukesh.order.dto.*;
import com.mukesh.order.entity.AppUser;
import com.mukesh.order.entity.security.RefreshToken;
import com.mukesh.order.exception.UserNotFoundException;
import com.mukesh.order.repository.UserRepository;
import com.mukesh.order.service.JwtService;
import com.mukesh.order.service.RefreshTokenService;
import com.mukesh.order.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final RefreshTokenService refreshTokenService;

    private final AuditService auditService;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {

        SessionInfo sessionInfo =
                SessionUtil.extract(httpRequest);

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

        } catch (Exception ex) {

            auditService.logLoginFailure(
                    request.username(),
                    sessionInfo
            );

            throw ex;
        }

        AppUser user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        )
                );

        String accessToken =
                jwtService.generateToken(user);

        String refreshToken =
                refreshTokenService.createRefreshToken(
                        user,
                        sessionInfo
                );

        auditService.logLoginSuccess(
                user,
                sessionInfo
        );

        return ResponseEntity.ok(
                new LoginResponse(
                        accessToken,
                        refreshToken,
                        "Bearer",
                        jwtExpiration / 1000
                )
        );

    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest
    ) {

        RefreshToken oldToken =
                refreshTokenService.findByToken(
                        request.refreshToken()
                );

        refreshTokenService.verifyExpiration(oldToken);

        refreshTokenService.verifyNotRevoked(oldToken);

        refreshTokenService.updateLastUsed(oldToken);

        AppUser user =
                oldToken.getUser();

        refreshTokenService.revokeToken(oldToken);

        String accessToken =
                jwtService.generateToken(user);

        SessionInfo sessionInfo =
                SessionUtil.extract(httpRequest);

        String newRefreshToken =
                refreshTokenService.createRefreshToken(
                        user,
                        sessionInfo
                );

        auditService.logTokenRefresh(
                user,
                sessionInfo
        );

        return ResponseEntity.ok(
                new RefreshTokenResponse(
                        accessToken,
                        newRefreshToken,
                        "Bearer",
                        jwtExpiration / 1000
                )
        );

    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody LogoutRequest request
    ) {

        RefreshToken token =
                refreshTokenService.findByToken(
                        request.refreshToken()
                );

        refreshTokenService.logout(
                request.refreshToken()
        );

        auditService.logLogout(
                token
        );

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/sessions")
    public ResponseEntity<List<SessionResponse>> getSessions(
            Authentication authentication
    ) {

        AppUser user =
                getCurrentUser(authentication);

        return ResponseEntity.ok(
                refreshTokenService.getSessions(
                        user,
                        null
                )
        );

    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> revokeSession(
            @PathVariable UUID sessionId,
            Authentication authentication
    ) {

        AppUser user =
                getCurrentUser(authentication);

        refreshTokenService.revokeSession(
                sessionId,
                user
        );

        auditService.logSessionRevoked(
                user,
                sessionId
        );

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(
            Authentication authentication
    ) {

        AppUser user =
                getCurrentUser(authentication);

        refreshTokenService.logoutAll(
                user
        );

        auditService.logLogoutAll(
                user
        );

        return ResponseEntity.noContent().build();

    }

    private AppUser getCurrentUser(
            Authentication authentication
    ) {

        return userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        )
                );

    }

}
