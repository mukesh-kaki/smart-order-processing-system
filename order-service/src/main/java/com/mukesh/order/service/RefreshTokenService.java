package com.mukesh.order.service;

import com.mukesh.order.dto.SessionInfo;
import com.mukesh.order.dto.SessionResponse;
import com.mukesh.order.entity.AppUser;
import com.mukesh.order.entity.security.RefreshToken;
import com.mukesh.order.exception.InvalidRefreshTokenException;
import com.mukesh.order.exception.RefreshTokenExpiredException;
import com.mukesh.order.exception.RefreshTokenRevokedException;
import com.mukesh.order.repository.RefreshTokenRepository;
import com.mukesh.order.mapper.SessionMapper;
import com.mukesh.order.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Creates a refresh token for a device/session.
     */
    public String createRefreshToken(
            AppUser user,
            SessionInfo sessionInfo
    ) {

        String refreshToken = UUID.randomUUID().toString();

        String tokenHash = HashUtil.sha256(refreshToken);

        RefreshToken entity = RefreshToken.builder()
                .tokenHash(tokenHash)
                .expiryDate(
                        Instant.now().plusMillis(refreshTokenDurationMs)
                )
                .user(user)
                .deviceName(sessionInfo.deviceName())
                .userAgent(sessionInfo.userAgent())
                .ipAddress(sessionInfo.ipAddress())
                .deviceId(sessionInfo.deviceId())
                .lastUsedAt(Instant.now())
                .build();

        refreshTokenRepository.save(entity);

        return refreshToken;
    }

    /**
     * Finds refresh token by token value.
     */
    public RefreshToken findByToken(String refreshToken) {

        String tokenHash = HashUtil.sha256(refreshToken);

        return refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException(
                                "Refresh token not found."
                        ));
    }

    /**
     * Expiration validation.
     */
    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {

            token.setRevoked(true);
            token.setRevokedAt(Instant.now());

            refreshTokenRepository.save(token);

            throw new RefreshTokenExpiredException(
                    "Refresh token has expired."
            );
        }

        return token;
    }

    /**
     * Revocation validation.
     */
    public RefreshToken verifyNotRevoked(RefreshToken token) {

        if (token.isRevoked()) {

            throw new RefreshTokenRevokedException(
                    "Refresh token has been revoked."
            );
        }

        return token;
    }

    /**
     * Updates last activity timestamp.
     */
    public void updateLastUsed(RefreshToken token) {

        token.setLastUsedAt(Instant.now());

        refreshTokenRepository.save(token);
    }

    /**
     * Revokes a single token.
     */
    public void revokeToken(RefreshToken token) {

        if (!token.isRevoked()) {

            token.setRevoked(true);
            token.setRevokedAt(Instant.now());

            refreshTokenRepository.save(token);

        }

    }

    /**
     * Logout current session using refresh token.
     */
    public void logout(String refreshToken) {

        RefreshToken token = findByToken(refreshToken);

        revokeToken(token);

    }

    /**
     * Logout all sessions.
     */
    public void logoutAll(AppUser user) {

        List<RefreshToken> tokens =
                refreshTokenRepository.findAllByUser(user);

        tokens.stream()
                .filter(token -> !token.isRevoked())
                .forEach(token -> {

                    token.setRevoked(true);
                    token.setRevokedAt(Instant.now());

                });

        refreshTokenRepository.saveAll(tokens);

    }

    /**
     * Returns all user sessions.
     */
    public List<SessionResponse> getSessions(
            AppUser user,
            String currentDeviceId
    ) {

        return refreshTokenRepository
                .findAllByUser(user)
                .stream()
                .map(token ->
                        SessionMapper.toResponse(
                                token,
                                currentDeviceId
                        )
                )
                .toList();

    }

    /**
     * Logout a specific device.
     */
    public void revokeSession(
            UUID sessionId,
            AppUser user
    ) {

        RefreshToken token =
                refreshTokenRepository
                        .findByIdAndUser(sessionId, user)
                        .orElseThrow(() ->
                                new InvalidRefreshTokenException(
                                        "Session not found."
                                ));

        revokeToken(token);

    }

}