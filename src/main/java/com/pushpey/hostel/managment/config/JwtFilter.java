package com.pushpey.hostel.managment.config;

import com.pushpey.hostel.managment.entity.User;
import com.pushpey.hostel.managment.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n========== JWT FILTER RUNNING ==========");

        String path = request.getServletPath();

        System.out.println("REQUEST PATH: " + path);

        if (path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {

            filterChain.doFilter(request, response);
            return;
        }

//        String path = request.getRequestURI();

        System.out.println("REQUEST PATH: " + path);

        // 🔥 Skip auth endpoints
        if (path.startsWith("/auth")) {
            System.out.println("AUTH ENDPOINT - SKIPPING FILTER");
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        System.out.println("AUTH HEADER: " + header);

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            System.out.println("TOKEN: " + token);

            try {

                //  Extract username
                String username = jwtUtil.extractUsername(token);

                System.out.println("USERNAME FROM TOKEN: " + username);

                //  Fetch user from DB
                User user = userRepo.findByUsername(username).orElse(null);

                System.out.println("USER FOUND IN DB: " + user);

                if (user != null) {

                    System.out.println("USER ROLE: " + user.getRole().name());

                    // 🔥 IMPORTANT ROLE FIX
                    List<SimpleGrantedAuthority> authorities =
                            List.of(
                                    new SimpleGrantedAuthority(
                                            "ROLE_" + user.getRole().name()
                                    )
                            );

                    System.out.println("AUTHORITIES: " + authorities);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    authorities
                            );

                    // 🔥 Set authentication
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    System.out.println("AUTHENTICATION SET SUCCESSFULLY ✅");
                }

            } catch (Exception e) {

                System.out.println("JWT ERROR ❌");
                e.printStackTrace();
            }
        } else {

            System.out.println("NO VALID AUTH HEADER FOUND ❌");
        }

        System.out.println("FILTER COMPLETED ✅");

        filterChain.doFilter(request, response);
    }
}