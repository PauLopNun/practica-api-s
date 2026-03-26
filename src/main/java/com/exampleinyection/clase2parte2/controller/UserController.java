package com.exampleinyection.clase2parte2.controller;

import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.dto.UserRequest;
import com.exampleinyection.clase2parte2.model.User;
import com.exampleinyection.clase2parte2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@Validated
public class UserController {

    private final UserService userService;
    private final AppConfig appConfig;

    public UserController(UserService userService, AppConfig appConfig) {
        this.userService = userService;
        this.appConfig = appConfig;
    }

    @GetMapping
    public List<User> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getPaginatedUsers(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public User createUser(@RequestBody @Valid UserRequest request) {
        return userService.saveUser(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        User updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/user/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String nombre) {
        List<User> usersFound = userService.searchByName(nombre);
        return ResponseEntity.ok(usersFound);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<User>> createMultipleUsers(@RequestBody @Valid List<UserRequest> requests) {
        return ResponseEntity.ok(userService.saveMultipleUsers(requests));
    }

    @GetMapping("/config")
    public ResponseEntity<AppConfig> getConfig() {
        return ResponseEntity.ok(appConfig);
    }
}