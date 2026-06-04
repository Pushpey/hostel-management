package com.pushpey.hostel.managment.controller;

import com.pushpey.hostel.managment.config.JwtUtil;
import com.pushpey.hostel.managment.entity.User;
import com.pushpey.hostel.managment.repository.UserRepository;
import com.pushpey.hostel.managment.service.AuthService;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return authService.login(user.getUsername(), user.getPassword());
    }

    @GetMapping("/oauth-success")
    public String success(Authentication authentication) {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        System.out.println(oauthUser.getAttributes());


        return authService.oauthLogin(oauthUser);
    }
}

