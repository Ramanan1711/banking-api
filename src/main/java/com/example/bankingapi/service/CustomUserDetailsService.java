package com.example.bankingapi.service;

import com.example.bankingapi.entity.User;
import com.example.bankingapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user from the repository
        User userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Get the role from the user entity
        String role = userEntity.getRole();

        // Validate the role (only ADMIN and USER are allowed)
        if (!role.equals("ADMIN") && !role.equals("USER")) {
            throw new UsernameNotFoundException("User has an invalid role: " + role);
        }

        // Map the entity to Spring Security's UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword()) // Password must already be hashed
                .roles(role) // Set the role directly as ADMIN or USER
                .build();
    }
}
