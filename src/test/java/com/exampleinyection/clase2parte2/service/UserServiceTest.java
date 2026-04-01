package com.exampleinyection.clase2parte2.service;

import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.dto.UserRequest;
import com.exampleinyection.clase2parte2.exception.UserNotFoundException;
import com.exampleinyection.clase2parte2.model.Allergy;
import com.exampleinyection.clase2parte2.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AppConfig appConfig;

    @InjectMocks
    private UserService userService;

    @Test
    void testSaveUserWithNullName() {
        when(appConfig.getCommon().getDefaults().getName()).thenReturn("DefaultName");
        when(appConfig.getCommon().getDefaults().getAge()).thenReturn(18);

        UserRequest request = new UserRequest(null, -1, null);
        User user = userService.saveUser(request);

        assertEquals("DefaultName", user.getName());
        assertEquals(18, user.getAge());
    }

    @Test
    void testSaveUserWithNullAgeAndEmptyName() {
        when(appConfig.getCommon().getDefaults().getName()).thenReturn("DefaultName");
        when(appConfig.getCommon().getDefaults().getAge()).thenReturn(18);

        UserRequest request = new UserRequest("", null, null);
        User user = userService.saveUser(request);

        assertEquals("DefaultName", user.getName());
        assertEquals(18, user.getAge());
    }

    @Test
    void testSaveUserWithValidData() {
        UserRequest request = new UserRequest("Pepe", 20, null);
        User user = userService.saveUser(request);

        assertEquals("Pepe", user.getName());
        assertEquals(20, user.getAge());
    }

    @Test
    void testGetUserByIdNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void testDeleteUser() {
        UserRequest request = new UserRequest("Pepe", 20, null);
        User savedUser = userService.saveUser(request);

        userService.deleteUser(savedUser.getId());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(savedUser.getId()));
    }

    @Test
    void testUpdateUser() {
        User user = userService.saveUser(new UserRequest("Pepe", 20, null));

        UserRequest updateRequest = new UserRequest("Paco", 22, null);
        User updatedUser = userService.updateUser(user.getId(), updateRequest);

        assertEquals("Paco", updatedUser.getName());
        assertEquals(22, updatedUser.getAge());
    }

    @Test
    void testUpdateUserPartialAndEmpty() {
        User user = userService.saveUser(new UserRequest("Pepe", 20, null));

        UserRequest partialRequest = new UserRequest("   ", 0, List.of(new Allergy("Peanuts", 3)));
        User updatedUser = userService.updateUser(user.getId(), partialRequest);

        assertEquals("Pepe", updatedUser.getName());
        assertEquals(20, updatedUser.getAge());
        assertNotNull(updatedUser.getAllergies());

        UserRequest nullRequest = new UserRequest(null, null, null);
        User updatedUserWithNulls = userService.updateUser(user.getId(), nullRequest);

        assertEquals("Pepe", updatedUserWithNulls.getName());
        assertEquals(20, updatedUserWithNulls.getAge());
    }

    @Test
    void testUpdateUserDisabled() {
        when(appConfig.getUpdate().isDisabled()).thenReturn(true);
        when(appConfig.getUpdate().getMessage()).thenReturn("Disabled");

        User savedUser = userService.saveUser(new UserRequest("Pepe", 20, null));
        UserRequest updateRequest = new UserRequest("Paco", 22, null);

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(savedUser.getId(), updateRequest));
    }

    @Test
    void testSearchByName() {
        userService.saveUser(new UserRequest("Pepe", 20, null));
        userService.saveUser(new UserRequest("Pedro", 20, null));

        assertEquals(2, userService.searchByName("pe").size());
    }

    @Test
    void testSaveMultipleUsers() {
        List<UserRequest> requests = List.of(
                new UserRequest("Pepe", 20, null),
                new UserRequest("Paco", 20, null)
        );

        List<User> savedUsers = userService.saveMultipleUsers(requests);
        assertEquals(2, savedUsers.size());
    }

    @Test
    void testGetPaginatedUsers() {
        when(appConfig.getCommon().getPagination().getMaxSize()).thenReturn(100);

        userService.saveUser(new UserRequest("Pepe", 20, null));
        userService.saveUser(new UserRequest("Paco", 20, null));

        assertEquals(2, userService.getPaginatedUsers(-1, 10).size());
        assertEquals(2, userService.getPaginatedUsers(1, 10).size());
        assertEquals(1, userService.getPaginatedUsers(2, 1).size());

        assertEquals(appConfig, userService.getAppConfig());

        userService.deleteAllUsers();
        assertEquals(0, userService.getUsers().size());
        userService.getNextId();
    }

    @Test
    void testGetUsersWithRepository() {
        User user1 = new User(1L, "TestUser", 20, List.of(new Allergy(1L, "Dust", 2, null)));
        User user2 = new User(2L, "TestUser2", 30, null);

        // When repository is provided (injected via constructor, let's mock it)
        com.exampleinyection.clase2parte2.repository.UserRepository repo = org.mockito.Mockito.mock(com.exampleinyection.clase2parte2.repository.UserRepository.class);
        UserService serviceWithRepo = new UserService(appConfig, repo);

        org.mockito.Mockito.when(repo.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = serviceWithRepo.getUsers();

        assertEquals(2, result.size());
        // Verify mapping worked without cycles
        assertNull(result.get(0).getAllergies().get(0).getUser());
        assertEquals("Dust", result.get(0).getAllergies().get(0).getName());
        assertEquals(2, result.get(0).getAllergies().get(0).getSeverity());
        assertNull(result.get(1).getAllergies());
    }

    @Test
    void testGetUsersWithoutRepository() {
        // As defined in the current mock setup, userRepository is injected but let's test the fallback
        UserService serviceNoRepo = new UserService(appConfig, null);
        serviceNoRepo.saveUser(new UserRequest("NoRepoUser", 25, List.of(new Allergy("Peanuts", 5))));

        List<User> result = serviceNoRepo.getUsers();
        assertEquals(1, result.size());
        assertEquals("NoRepoUser", result.get(0).getName());
        assertNull(result.get(0).getAllergies().get(0).getUser());
    }
}
