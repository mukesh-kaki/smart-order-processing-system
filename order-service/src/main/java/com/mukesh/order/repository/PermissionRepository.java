package com.mukesh.order.repository;


import com.mukesh.order.entity.security.Permission;
import com.mukesh.order.entity.security.PermissionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    Optional<Permission> findByName(PermissionName name);

    boolean existsByName(PermissionName name);

}