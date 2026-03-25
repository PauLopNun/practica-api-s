package com.exampleinyection.clase2parte2.service;

import com.exampleinyection.clase2parte2.model.User;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public User getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        users.remove(user);
    }

    public User updateUser(Long id, User userDetails) {

        User existingUser = getUserById(id);
        existingUser.setNombre(userDetails.getNombre());
        existingUser.setEdad(userDetails.getEdad());
        existingUser.setAllergy(userDetails.getAllergy());

        return existingUser;
    }

    public List<User> searchByName(String nombre) {
        return users.stream()
                .filter(user -> user.getNombre() != null &&
                        user.getNombre().equalsIgnoreCase(nombre))
                .collect(Collectors.toList());
    }

    public List<User> saveMultipleUsers(List<User> newUsers) {
        return newUsers.stream()
                .map(this::saveUser)
                .toList();
    }
}