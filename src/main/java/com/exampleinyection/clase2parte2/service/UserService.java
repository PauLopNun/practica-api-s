package com.exampleinyection.clase2parte2.service;

import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.dto.UserRequest;
import com.exampleinyection.clase2parte2.exception.UserNotFoundException;
import com.exampleinyection.clase2parte2.model.Allergy;
import com.exampleinyection.clase2parte2.model.User;
import com.exampleinyection.clase2parte2.repository.UserRepository;
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
    private final UserRepository userRepository;

    public UserService(AppConfig appConfig, UserRepository userRepository) {
        this.appConfig = appConfig;
        this.userRepository = userRepository;
    }

    public User saveUser(UserRequest request) {
        String userNameWithDefault = (request.name() == null || request.name().isBlank())
                ? appConfig.getCommon().getDefaults().getName()
                : request.name();

        int userAgeWithDefault = (request.age() == null || request.age() <= 0)
                ? appConfig.getCommon().getDefaults().getAge()
                : request.age();

        User newUser = new User();
        newUser.setName(userNameWithDefault);
        newUser.setAge(userAgeWithDefault);

        if (request.allergies() != null) {
            newUser.setAllergies(request.allergies());
            for (Allergy allergy : request.allergies()) {
                allergy.setUser(newUser);
            }
        }

        if (userRepository != null) {
            return userRepository.save(newUser);
        } else {
            newUser.setId(nextId++);
            users.add(newUser);
            return newUser;
        }
    }

    public User getUserById(Long id) {
        if (userRepository != null) {
            return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No existe el usuario con id: " + id));
        }
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("No existe el usuario con id: " + id));
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        if (userRepository != null) {
            userRepository.delete(user);
        } else {
            users.remove(user);
        }
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
            for (Allergy allergy : request.allergies()) {
                allergy.setUser(existingUser);
            }
        }

        if (userRepository != null) {
            return userRepository.save(existingUser);
        }

        return existingUser;
    }

    public List<User> searchByName(String nameSearchTerm) {
        List<User> sourceUsers = userRepository != null 
                ? userRepository.findByNameContainingIgnoreCase(nameSearchTerm)
                : users.stream()
                    .filter(user -> StringUtils.containsIgnoreCase(user.getName(), nameSearchTerm))
                    .toList();

        return sourceUsers.stream()
                .map(this::mapUserWithoutCycles)
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

        List<User> sourceUsers = userRepository != null ? userRepository.findAll() : users;

        return sourceUsers.stream()
                .skip(skip)
                .limit(finalSize)
                .toList();
    }

    public void updateUserName(Long id, String name) {
        int updated = userRepository.updateNameById(id, name);
        if (updated == 0) throw new UserNotFoundException("No existe el usuario con id: " + id);
    }

    public void deleteAllUsers() {
        if (userRepository != null) {
            userRepository.deleteAll();
        } else {
            users.clear();
        }
    }

    public List<User> getUsers() {
        List<User> sourceUsers = userRepository != null ? userRepository.findAll() : users;
        return sourceUsers.stream()
                .map(this::mapUserWithoutCycles)
                .toList();
    }

    private User mapUserWithoutCycles(User user) {
        List<Allergy> mappedAllergies = user.getAllergies() == null
                ? null
                : user.getAllergies().stream().map(this::mapAllergyWithoutUser).toList();

        return new User(user.getId(), user.getName(), user.getAge(), mappedAllergies);
    }

    private Allergy mapAllergyWithoutUser(Allergy allergy) {
        Allergy mapped = new Allergy();
        mapped.setId(allergy.getId());
        mapped.setName(allergy.getName());
        mapped.setSeverity(allergy.getSeverity());
        return mapped;
    }
}
