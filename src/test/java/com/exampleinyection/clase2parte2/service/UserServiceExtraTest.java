package com.exampleinyection.clase2parte2.service;
import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.dto.UserRequest;
import com.exampleinyection.clase2parte2.model.Allergy;
import com.exampleinyection.clase2parte2.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
class UserServiceExtraTest {
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
    void testBlankName() {
        UserRequest request = new UserRequest("   ", 25, List.of(new Allergy("Pollen", 2)));
        User saved = userService.saveUser(request);
        assertEquals("DefaultName", saved.getName(), "Empty blank names should use default");
    }
    @Test
    void testNegativeAge() {
        UserRequest request = new UserRequest("John", -10, null);
        User saved = userService.saveUser(request);
        assertEquals(18, saved.getAge(), "Negative age should use default");
    }
    @Test
    void testZeroSizePagination() {
        userService.saveUser(new UserRequest("A", 20, null));
        List<User> list = userService.getPaginatedUsers(1, 0);
        assertEquals(0, list.size());
    }
    @Test
    void testExceedMaxSizePagination() {
        userService.saveUser(new UserRequest("A", 20, null));
        userService.saveUser(new UserRequest("B", 20, null));
        appConfig.getPagination().setMaxSize(1);
        List<User> list = userService.getPaginatedUsers(1, 10);
        assertEquals(1, list.size());
    }
}
