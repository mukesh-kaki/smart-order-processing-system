package com.mukesh.order.config;

import com.mukesh.order.entity.*;
import com.mukesh.order.entity.security.Permission;
import com.mukesh.order.entity.security.PermissionName;
import com.mukesh.order.entity.security.Role;
import com.mukesh.order.entity.security.RoleName;
import com.mukesh.order.repository.*;
import com.mukesh.order.entity.security.RolePermissionMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SecurityDataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        initializePermissions();

        initializeRoles();

        initializeAdminUser();
    }

    private void initializePermissions() {

        for (PermissionName permissionName : PermissionName.values()) {

            if (!permissionRepository.existsByName(permissionName)) {

                Permission permission = Permission.builder()
                        .name(permissionName)
                        .description(permissionName.name())
                        .build();

                permissionRepository.save(permission);
            }
        }
    }

    private void initializeRoles() {

        for (RoleName roleName : RoleName.values()) {

            if (!roleRepository.existsByName(roleName)) {

                Set<Permission> permissions =
                        RolePermissionMapping
                                .getPermissions(roleName)
                                .stream()
                                .map(permissionName ->
                                        permissionRepository
                                                .findByName(permissionName)
                                                .orElseThrow())
                                .collect(Collectors.toSet());

                Role role = Role.builder()
                        .name(roleName)
                        .description(roleName.name())
                        .permissions(permissions)
                        .build();

                roleRepository.save(role);
            }
        }
    }

    private void initializeAdminUser() {

        if (userRepository.findByUsername("admin").isPresent()) {
            return;
        }

        Role adminRole =
                roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow();

        AppUser admin = AppUser.builder()
                .userName("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles(Set.of(adminRole))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        userRepository.save(admin);
    }
}