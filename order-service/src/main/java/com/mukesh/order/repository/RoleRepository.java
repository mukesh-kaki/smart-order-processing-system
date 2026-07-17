package com.mukesh.order.repository;


import com.mukesh.order.entity.security.Role;
import com.mukesh.order.entity.security.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(RoleName name);

    boolean existsByName(RoleName name);

}