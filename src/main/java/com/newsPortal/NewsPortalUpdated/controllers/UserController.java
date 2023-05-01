package com.newsPortal.NewsPortalUpdated.controllers;

import com.newsPortal.NewsPortalUpdated.dto.UserDTO;
import com.newsPortal.NewsPortalUpdated.models.User;
import com.newsPortal.NewsPortalUpdated.services.UserService;
import com.newsPortal.NewsPortalUpdated.util.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        return ResponseEntity.ok(userService.findAll().stream().map(this::mapToUserDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(mapToUserDTO(userService.findByEmail(email)));
    }

    @PutMapping("/user/email")
    public ResponseEntity<Object> updateUserEmail(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Have errors in request - " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        userService.updateEmailById(mapToUser(userDTO));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/password")
    public ResponseEntity<Object> updateUserPassword(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || userDTO.getPassword() == null) {
            logger.info("Have errors in request - " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userService.updatePasswordById(mapToUser(userDTO));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/role")
    public ResponseEntity<Object> updateUserRole(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Have errors in request - " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        userService.updateRoleByUserId(mapToUser(userDTO));
        return ResponseEntity.ok().build();
    }

    private User mapToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO mapToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        logger.info("Handle ConstraintViolationException - " + exception);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
