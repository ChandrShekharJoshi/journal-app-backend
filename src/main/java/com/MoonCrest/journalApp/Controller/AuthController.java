package com.MoonCrest.journalApp.Controller;

import com.MoonCrest.journalApp.Entity.User;
import com.MoonCrest.journalApp.service.JwtService;
import com.MoonCrest.journalApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.MoonCrest.journalApp.dto.RegisterRequest;
import jakarta.validation.Valid;
import com.MoonCrest.journalApp.Entity.Role;

import com.MoonCrest.journalApp.dto.LoginRequest;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        if (userService.findByUserName(request.getUserName()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        User user = new User();

        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());

        // Public registration can create USER only
        user.setRole(Role.USER);

        userService.saveEntry(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }



    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequest request) {

        System.out.println("========== LOGIN API HIT ==========");
        System.out.println("USERNAME: " + request.getUserName());

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUserName(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(token);
    }
}