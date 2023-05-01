package com.newsPortal.NewsPortalUpdated.controllers;

import com.newsPortal.NewsPortalUpdated.dto.AuthenticationDTO;
import com.newsPortal.NewsPortalUpdated.dto.UserDTO;
import com.newsPortal.NewsPortalUpdated.models.User;
import com.newsPortal.NewsPortalUpdated.security.JWTUtil;
import com.newsPortal.NewsPortalUpdated.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService, JWTUtil jwtUtil, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> performLogin(@RequestBody @Valid AuthenticationDTO authenticationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Have errors in request - " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getEmail(), authenticationDTO.getPassword());
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException exception) {
            logger.info("Invalid email or password - " + exception);
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid email or password"));
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(Map.of("jwt_token", token));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> performRegistration(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Have errors in request - " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userService.createUser(mapToUser(userDTO));
        return ResponseEntity.ok().build();
    }

    private User mapToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
