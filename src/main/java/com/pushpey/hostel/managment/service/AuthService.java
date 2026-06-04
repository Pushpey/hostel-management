package com.pushpey.hostel.managment.service;

import com.pushpey.hostel.managment.config.JwtUtil;
import com.pushpey.hostel.managment.entity.Role;
import com.pushpey.hostel.managment.entity.User;
import com.pushpey.hostel.managment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.STUDENT);
        }
        userRepo.save(user);
        return "User registered successfully!";
    }
    public String login(String username, String password) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }
        return jwtUtil.generateToken(user.getUsername());
    }

    public String oauthLogin(OAuth2User oauthUser) {

        String email = oauthUser.getAttribute("email");

        User user = userRepo.findByUsername(email).orElse(null);

        if(user == null) {

            user = new User();

            user.setUsername(email);

            user.setPassword("OAUTH_USER");

            user.setRole(Role.STUDENT);

            userRepo.save(user);
        }

        return jwtUtil.generateToken(user.getUsername());
    }
}
