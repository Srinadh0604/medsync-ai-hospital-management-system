package com.srinadh.medsync.service;

import com.srinadh.medsync.dto.LoginRequest;
import com.srinadh.medsync.dto.LoginResponse;
import com.srinadh.medsync.dto.RegisterRequest;
import com.srinadh.medsync.entity.Role;
import com.srinadh.medsync.entity.User;
import com.srinadh.medsync.exception.EmailAlreadyExistsException;
import com.srinadh.medsync.exception.ResourceNotFoundException;
import com.srinadh.medsync.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditService auditService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuditService auditService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.auditService = auditService;
    }
  // Main code
//    public User register(RegisterRequest request) {
//
//        if (userRepository.existsByEmail(
//                request.getEmail())) {
//
//            throw new EmailAlreadyExistsException(
//                    "Email already exists");
//        }
//
//        User user = new User();
//
//        user.setName(request.getName());
//
//        user.setEmail(request.getEmail());
//
//        user.setPassword(
//                passwordEncoder.encode(
//                        request.getPassword()
//                )
//        );
//
//        Role role = Role.PATIENT;
//
//        if (request.getRole() != null &&
//                !request.getRole().isBlank()) {
//
//            role = Role.valueOf(
//                    request.getRole()
//            );
//        }
//
//        user.setRole(role);
//
//        return userRepository.save(user);
//    }


    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Always assign a safe default role
        user.setRole(Role.PATIENT);

        User savedUser = userRepository.save(user);

        auditService.logAction(
                savedUser.getEmail(),
                "REGISTER"
        );

        return savedUser;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(
                        request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: "
                                        + request.getEmail()));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "Invalid password");
        }

        auditService.logAction(
                user.getEmail(),
                "LOGIN");

        String token =
                jwtService.generateToken(user);

        return new LoginResponse(
                token,
                user.getRole().name(),
                user.getName()
        );
    }


}
