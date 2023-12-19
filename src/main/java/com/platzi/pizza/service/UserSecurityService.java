package com.platzi.pizza.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.platzi.pizza.persistence.entity.UserEntity;
import com.platzi.pizza.persistence.entity.UserRoleEntity;
import com.platzi.pizza.persistence.repository.UserRepository;

import java.util.List;
import java.util.ArrayList;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        String[] roles = userEntity.getRoles().stream().map(UserRoleEntity::getRole).toArray(String[]::new);

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                // .roles("ADMIN")
                // .roles(roles)
                .authorities(this.grantedAuthorities(roles))
                .accountLocked(userEntity.getLocked())
                .disabled(userEntity.getDisabled())
                .build();
    }

    private String[] getAuthorities(String role) {
        if (role.equals("ADMIN") || role.equals("CUSTOMER")) {
            return new String[] { "random_order" };
        }

        return new String[] {};
    }

    // La idea general de este método es que se puedan agregar más roles y
    // authorities, por ejemplo, si se desea agregar un nuevo rol, se debe agregar a
    // la lista de roles y authorities y se debe agregar el método getAuthorities
    // con el nuevo rol y sus authorities correspondientes.
    private List<GrantedAuthority> grantedAuthorities(String[] roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

            for (String authority : this.getAuthorities(role)) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }

        return authorities;
    }

}
