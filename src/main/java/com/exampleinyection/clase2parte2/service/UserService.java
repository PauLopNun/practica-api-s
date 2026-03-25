package com.exampleinyection.clase2parte2.service;

import com.exampleinyection.clase2parte2.model.User;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class UserService {

    private final List<User> users = new ArrayList<>();
    private long nextId = 1;

    public User saveUser(User user) {
        User newUser = new User(
                nextId++,
                user.getNombre(),
                user.getEdad(),
                user.getAllergy()
        );
        users.add(newUser);
        return newUser;
    }
}