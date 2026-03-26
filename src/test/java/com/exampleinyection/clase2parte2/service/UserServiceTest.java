package com.exampleinyection.clase2parte2.service;
import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.dto.UserRequest;
import com.exampleinyection.clase2parte2.exception.UserNotFoundException;
import com.exampleinyection.clase2parte2.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class UserServiceTest {
    private UserService userService;
    private AppConfig appConfig;
    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
        AppConfig.DefaultSettings defaults = new AppConfig.DefaultSettings();
        defaults.setName("DefaultName");
        defaults.setAge(18);
        appConfig.setDefaults(defaults);
        AppConfig.UpdateSettings update = new AppConfig.UpdateSettings();
        update.setDisabled(false);
        appConfig.setUpdate(update);
        AppConfig.PaginationSettings pagination = new AppConfig.PaginationSettings();
        pagination.setMaxSize(100);
        appConfig.setPagination(pagination);
        userService = new UserService(appConfig);
    }
    @Test
    void saveUser_withNullName() {
        UserRequest request = new UserRequest(null, -1, null);
        User user = userService.saveUser(request);
        assertEquals("DefaultName", user.getName());
        assertEquals(18, user.getAge());
    }

    @Test
    void saveUser_withNullAgeAndEmptyName() {
        UserRequest request = new UserRequest("", null, null);
        User user = userService.saveUser(request);
        assertEquals("DefaultName", user.getName());
        assertEquals(18, user.getAge());
    }

    @Test
    void saveUser_withValidData() {
        UserRequest request = new UserRequest("Pepe", 20, null);
        User user = userService.saveUser(request);
        assertEquals("Pepe", user.getName());
        assertEquals(20, user.getAge());
    }
    @Test
    void getUserById_notFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }
    @Test
    void deleteUser() {
        User u = userService.saveUser(new UserRequest("Pepe", 20, null));
        userService.deleteUser(u.getId());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(u.getId()));
    }
    @Test
    void updateUser() {
        User user = userService.saveUser(new UserRequest("Pepe", 20, null));
        User updatedUser = userService.updateUser(user.getId(), new UserRequest("Paco", 22, null));
        assertEquals("Paco", updatedUser.getName());
        assertEquals(22, updatedUser.getAge());
    }

    @Test
    void updateUser_partialAndEmpty() {
        User user = userService.saveUser(new UserRequest("Pepe", 20, null));
        
        User updatedUser = userService.updateUser(user.getId(), new UserRequest("   ", 0, java.util.List.of(new com.exampleinyection.clase2parte2.model.Allergy("Peanuts", 3))));
        assertEquals("Pepe", updatedUser.getName());
        assertEquals(20, updatedUser.getAge());
        assertNotNull(updatedUser.getAllergies());

        User updatedUserWithNulls = userService.updateUser(user.getId(), new UserRequest(null, null, null));
        assertEquals("Pepe", updatedUserWithNulls.getName());
        assertEquals(20, updatedUserWithNulls.getAge());
    }

    @Test
    void updateUser_disabled() {
        appConfig.getUpdate().setDisabled(true);
        appConfig.getUpdate().setMessage("Disabled");
        User u = userService.saveUser(new UserRequest("Pepe", 20, null));
        assertThrows(ResponseStatusException.class, () -> userService.updateUser(u.getId(), new UserRequest("Paco", 22, null)));
    }
    @Test
    void searchByName() {
        userService.saveUser(new UserRequest("Pepe", 20, null));
        userService.saveUser(new UserRequest("Pedro", 20, null));
        assertEquals(2, userService.searchByName("pe").size());
    }
    @Test
    void saveMultipleUsers() {
        List<User> users = userService.saveMultipleUsers(List.of(
                new UserRequest("Pepe", 20, null),
                new UserRequest("Paco", 20, null)
        ));
        assertEquals(2, users.size());
    }
    @Test
    void getPaginatedUsers() {
        userService.saveUser(new UserRequest("Pepe", 20, null));
        userService.saveUser(new UserRequest("Paco", 20, null));
        assertEquals(2, userService.getPaginatedUsers(-1, 10).size());
        assertEquals(2, userService.getPaginatedUsers(1, 10).size());
        assertEquals(1, userService.getPaginatedUsers(2, 1).size());
        // extra checks for AppConfig getters not heavily used
        assertEquals(appConfig, userService.getAppConfig());
        userService.deleteAllUsers();
        assertEquals(0, userService.getUsers().size());
        userService.getNextId();
    }
}
