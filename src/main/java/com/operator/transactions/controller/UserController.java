package com.operator.transactions.controller;

import com.operator.transactions.dto.UserRequestDTO;
import com.operator.transactions.dto.UserResponseDTO;
import com.operator.transactions.exception.UserNotFoundException;
import com.operator.transactions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) throws UserNotFoundException {
        return ResponseEntity.ok(userService.createUser(userRequestDTO));
    }

    @GetMapping("/")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() throws UserNotFoundException {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long userId) throws UserNotFoundException {
        UserResponseDTO userEntity = userService.getUserById(userId);
        return userEntity != null ?
                ResponseEntity.ok(userEntity) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) throws UserNotFoundException {
        return userService.deleteUser(userId) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }


}
