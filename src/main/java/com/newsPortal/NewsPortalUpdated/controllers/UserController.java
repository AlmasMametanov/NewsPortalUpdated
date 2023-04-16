package com.newsPortal.NewsPortalUpdated.controllers;

import com.newsPortal.NewsPortalUpdated.dto.UserDTO;
import com.newsPortal.NewsPortalUpdated.models.User;
import com.newsPortal.NewsPortalUpdated.services.UserService;
import com.newsPortal.NewsPortalUpdated.util.EmailAlreadyExistsException;
import com.newsPortal.NewsPortalUpdated.util.ErrorResponse;
import com.newsPortal.NewsPortalUpdated.util.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
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

    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        userService.createUser(mapToUser(userDTO));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/email")
    public ResponseEntity<Object> updateUserEmail(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        userService.updateEmailById(mapToUser(userDTO));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/password")
    public ResponseEntity<Object> updateUserPassword(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        userService.updatePasswordById(mapToUser(userDTO));
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private ErrorResponse handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse handleUserNotFoundException(UserNotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    private User mapToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO mapToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
