package com.newsPortal.NewsPortalUpdated.controllers;

import com.newsPortal.NewsPortalUpdated.dto.AuthenticationDTO;
import com.newsPortal.NewsPortalUpdated.dto.UserDTO;
import com.newsPortal.NewsPortalUpdated.models.User;
import com.newsPortal.NewsPortalUpdated.security.JWTUtil;
import com.newsPortal.NewsPortalUpdated.services.UserService;
import com.newsPortal.NewsPortalUpdated.util.EmailAlreadyExistsException;
import com.newsPortal.NewsPortalUpdated.util.ErrorResponse;
import com.newsPortal.NewsPortalUpdated.util.UserNotFoundException;
import com.newsPortal.NewsPortalUpdated.util.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserValidator userValidator;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserValidator userValidator, UserService userService, JWTUtil jwtUtil, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userValidator = userValidator;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> performLogin(@RequestBody @Valid AuthenticationDTO authenticationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());

        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getEmail(), authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid email or password"));
        }
        String token = jwtUtil.generateToken(authenticationDTO.getEmail());
        return ResponseEntity.ok(Map.of("jwt_token", token));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> performRegistration(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        userService.createUser(mapToUser(userDTO));
        return ResponseEntity.ok().build();
    }

    private User mapToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private ErrorResponse handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse handleUserNotFoundException(UserNotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
}
