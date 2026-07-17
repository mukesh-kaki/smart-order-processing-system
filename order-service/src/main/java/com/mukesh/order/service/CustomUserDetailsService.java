package com.mukesh.order.service;

import com.mukesh.order.entity.AppUser;
import com.mukesh.order.entity.security.Permission;
import com.mukesh.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found : " + username
                        ));

        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities(buildAuthorities(user))
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }

    private Set<GrantedAuthority> buildAuthorities(AppUser user) {

        return user.getRoles()

                .stream()

                .flatMap(role -> Stream.concat(

                        Stream.of(
                                new SimpleGrantedAuthority(
                                        role.getName().name()
                                )
                        ),

                        role.getPermissions()

                                .stream()

                                .map(Permission::getName)

                                .map(Enum::name)

                                .map(SimpleGrantedAuthority::new)

                ))

                .collect(Collectors.toSet());
    }

}