package com.mukesh.order.repository;

import com.mukesh.order.entity.AppUser;
import com.mukesh.order.entity.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findAllByUser(AppUser user);

    Optional<RefreshToken> findByIdAndUser(
            UUID id,
            AppUser user
    );

}