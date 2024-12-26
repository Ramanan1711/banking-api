package com.example.bankingapi.controller;

import com.example.bankingapi.entity.User;
import com.example.bankingapi.repository.UserRepository;
import com.example.bankingapi.service.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public String login(@RequestBody User loginRequest) {
        try {
            System.out.println("Attempting login with username: " + loginRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Generate JWT Token
            String token = jwtTokenProvider.generateToken(authentication);
            return "Login successful! Token: " + token;
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return "Invalid username or password.";
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists.";
        }

        // Set the password and role from the request body
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Do not set a default role, let it come from the request
        if (user.getRole() == null || user.getRole().isEmpty()) {
            return "Role must be specified.";
        }

        userRepository.save(user);
        return "User registered successfully.";
    }

    @GetMapping("/profile")
    public Object getProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "Authorization header is missing or invalid.";
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtTokenProvider.extractUsername(token);

            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                return user.get();
            } else {
                return "User not found.";
            }
        } catch (Exception e) {
            return "Invalid token.";
        }
    }
}
