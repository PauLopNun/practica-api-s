package com.exampleinyection.clase2parte2.controller;

import com.exampleinyection.clase2parte2.model.User;
import com.exampleinyection.clase2parte2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userService.saveUser(user);
    }
}