package com.exampleinyection.clase2parte2.service;

import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.dto.UserRequest;
import com.exampleinyection.clase2parte2.exception.UserNotFoundException;
import com.exampleinyection.clase2parte2.model.User;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class UserService {

    private final List<User> users = new ArrayList<>();
    private long nextId = 1;
    private final AppConfig appConfig;

    public UserService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public User saveUser(UserRequest request) {
        String userNameWithDefault = (request.name() == null || request.name().isBlank())
                ? appConfig.getCommon().getDefaults().getName()
                : request.name();

        int userAgeWithDefault = (request.age() == null || request.age() <= 0)
                ? appConfig.getCommon().getDefaults().getAge()
                : request.age();

        User newUser = new User(
                nextId++,
                userNameWithDefault,
                userAgeWithDefault,
                request.allergies()
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

    public User updateUser(Long id, UserRequest request) {
        if (appConfig.getUpdate().isDisabled()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    appConfig.getUpdate().getMessage()
            );
        }

        User existingUser = getUserById(id);

        if (request.name() != null && !request.name().isBlank()) {
            existingUser.setName(request.name());
        }
        if (request.age() != null && request.age() > 0) {
            existingUser.setAge(request.age());
        }
        if (request.allergies() != null) {
            existingUser.setAllergies(request.allergies());
        }

        return existingUser;
    }

    public List<User> searchByName(String nameSearchTerm) {
        return users.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getName(), nameSearchTerm))
                .toList();
    }

    public List<User> saveMultipleUsers(List<UserRequest> newUsers) {
        return newUsers.stream()
                .map(this::saveUser)
                .toList();
    }

    public List<User> getPaginatedUsers(int page, int size) {
        if (page < 1) page = 1;
        int maxSize = appConfig.getCommon().getPagination().getMaxSize();
        int finalSize = Math.min(size, maxSize);
        int skip = (page - 1) * finalSize;

        return users.stream()
                .skip(skip)
                .limit(finalSize)
                .toList();
    }

    public void deleteAllUsers() {
        users.clear();
    }
}
