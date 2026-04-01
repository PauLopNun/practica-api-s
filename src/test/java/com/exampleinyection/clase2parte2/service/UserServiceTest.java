package com.exampleinyection.clase2parte2.service;

import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.dto.UserDTO;
import com.exampleinyection.clase2parte2.dto.UserRequest;
import com.exampleinyection.clase2parte2.exception.UserNotFoundException;
import com.exampleinyection.clase2parte2.model.Allergy;
import com.exampleinyection.clase2parte2.model.User;
import com.exampleinyection.clase2parte2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        assertNull(userService.getUserRepository());

        userService.deleteAllUsers();
        assertEquals(0, userService.getUsers().size());
        userService.getNextId();
    }

    @Test
    void testGetUsersWithRepository() {
        User user1 = new User(1L, "TestUser", 20, List.of(new Allergy(1L, "Dust", 2, null)));
        User user2 = new User(2L, "TestUser2", 30, null);

        UserRepository repo = mock(UserRepository.class);
        UserService serviceWithRepo = new UserService(appConfig, repo);

        when(repo.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = serviceWithRepo.getUsers();

        assertEquals(2, result.size());
        assertNull(result.get(0).getAllergies().get(0).getUsers());
        assertEquals("Dust", result.get(0).getAllergies().get(0).getName());
        assertEquals(2, result.get(0).getAllergies().get(0).getSeverity());
        assertNull(result.get(1).getAllergies());
    }

    @Test
    void testGetUsersWithoutRepository() {
        UserService serviceNoRepo = new UserService(appConfig, null);
        serviceNoRepo.saveUser(new UserRequest("NoRepoUser", 25, List.of(new Allergy("Peanuts", 5))));

        List<User> result = serviceNoRepo.getUsers();
        assertEquals(1, result.size());
        assertEquals("NoRepoUser", result.get(0).getName());
        assertNull(result.get(0).getAllergies().get(0).getUsers());
    }

    @Test
    void testGetUsersWithAllergies() {
        UserRepository repo = mock(UserRepository.class);
        UserService serviceWithRepo = new UserService(appConfig, repo);

        User user1 = new User(1L, "TestUser", 20, List.of(new Allergy(1L, "Dust", 2, null)));
        User user2 = new User(2L, "TestUser2", 30, null);

        when(repo.findAllWithAllergies()).thenReturn(List.of(user1, user2));

        List<UserDTO> result = serviceWithRepo.getUsersWithAllergies();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).allergies().size());
        assertEquals("Dust", result.get(0).allergies().get(0).name());
        assertEquals(2, result.get(0).allergies().get(0).severity());
        assertEquals(0, result.get(1).allergies().size());
    }

    @Test
    void testRepoBranchesCrudMethods() {
        UserRepository repo = mock(UserRepository.class);
        UserService svc = new UserService(appConfig, repo);

        User user = new User(1L, "Pepe", 20, null);
        when(repo.save(any(User.class))).thenReturn(user);
        when(repo.findById(1L)).thenReturn(Optional.of(user));
        when(repo.findByNameContainingIgnoreCase("pe")).thenReturn(List.of(user));
        when(appConfig.getCommon().getPagination().getMaxSize()).thenReturn(10);
        when(repo.findAll()).thenReturn(List.of(user));

        assertEquals("Pepe", svc.saveUser(new UserRequest("Pepe", 20, null)).getName());
        assertEquals("Pepe", svc.getUserById(1L).getName());
        svc.deleteUser(1L);
        verify(repo).delete(user);
        assertEquals(1, svc.searchByName("pe").size());
        assertEquals(1, svc.getPaginatedUsers(1, 10).size());
        svc.deleteAllUsers();
        verify(repo).deleteAll();
    }

    @Test
    void testRepoBranchGetUserByIdNotFound() {
        UserRepository repo = mock(UserRepository.class);
        UserService svc = new UserService(appConfig, repo);

        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> svc.getUserById(99L));
    }

    @Test
    void testRepoBranchUpdateUser() {
        UserRepository repo = mock(UserRepository.class);
        UserService svc = new UserService(appConfig, repo);

        User existing = new User(1L, "Pepe", 20, null);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(User.class))).thenReturn(new User(1L, "Paco", 22, null));

        User result = svc.updateUser(1L, new UserRequest("Paco", 22, null));
        assertEquals("Paco", result.getName());
    }

    @Test
    void testUpdateUserName() {
        UserRepository repo = mock(UserRepository.class);
        UserService serviceWithRepo = new UserService(appConfig, repo);

        when(repo.updateNameById(1L, "NewName")).thenReturn(1);
        serviceWithRepo.updateUserName(1L, "NewName");
        verify(repo).updateNameById(1L, "NewName");

        when(repo.updateNameById(99L, "X")).thenReturn(0);
        assertThrows(UserNotFoundException.class, () -> serviceWithRepo.updateUserName(99L, "X"));
    }
}
