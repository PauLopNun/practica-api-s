package com.exampleinyection.clase2parte2.service;

import com.exampleinyection.clase2parte2.exception.InvalidUserException;
import com.exampleinyection.clase2parte2.exception.UserNotFoundException;
import com.exampleinyection.clase2parte2.model.User;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
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

    public User getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("No existe el usuario con id: " + id));
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
                .filter(user -> StringUtils.containsIgnoreCase(user.getNombre(), nombre))
                .toList();
    }

    public List<User> saveMultipleUsers(List<User> newUsers) {
        return newUsers.stream()
                .map(this::saveUser)
                .toList();
    }

    public List<User> getPaginatedUsers(int page, int size) {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }

        int skip = (page - 1) * size;

        return users.stream()
                .skip(skip)
                .limit(size)
                .toList();
    }
}