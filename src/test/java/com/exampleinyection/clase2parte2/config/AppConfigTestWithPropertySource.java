package com.exampleinyection.clase2parte2.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {
    "app.name=Test Application Custom Name",
    "app.password=test_password_123",
    "app.update.disabled=true",
    "app.update.message=Updates disabled for testing",
    "app.pagination.max-size=25",
    "app.defaults.name=Default Test User",
    "app.defaults.age=21"
})
public class AppConfigTestWithPropertySource {

    @Autowired
    private AppConfig appConfig;

    @Test
    void testAppConfigWithCustomProperties() {
        assertEquals("Test Application Custom Name", appConfig.getName());
        assertEquals("test_password_123", appConfig.getPassword());
        assertEquals(true, appConfig.getUpdate().isDisabled());
        assertEquals("Updates disabled for testing", appConfig.getUpdate().getMessage());
        assertEquals(25, appConfig.getPagination().getMaxSize());
        assertEquals("Default Test User", appConfig.getDefaults().getName());
        assertEquals(21, appConfig.getDefaults().getAge());
    }

    @Test
    void testAppConfigPropertiesCanBeOverridden() {
        assertEquals("Test Application Custom Name", appConfig.getName());
        assertEquals("test_password_123", appConfig.getPassword());

        appConfig.setName("Updated Name");
        appConfig.setPassword("updated_password");

        assertEquals("Updated Name", appConfig.getName());
        assertEquals("updated_password", appConfig.getPassword());
    }

    @Test
    void testAppConfigPaginationSettings() {
        assertEquals(25, appConfig.getPagination().getMaxSize());

        appConfig.getPagination().setMaxSize(50);
        assertEquals(50, appConfig.getPagination().getMaxSize());
    }

    @Test
    void testAppConfigDefaultSettings() {
        assertEquals("Default Test User", appConfig.getDefaults().getName());
        assertEquals(21, appConfig.getDefaults().getAge());

        appConfig.getDefaults().setName("Different Default");
        appConfig.getDefaults().setAge(30);

        assertEquals("Different Default", appConfig.getDefaults().getName());
        assertEquals(30, appConfig.getDefaults().getAge());
    }

    @Test
    void testAppConfigUpdateSettings() {
        assertEquals(true, appConfig.getUpdate().isDisabled());
        assertEquals("Updates disabled for testing", appConfig.getUpdate().getMessage());

        appConfig.getUpdate().setDisabled(false);
        appConfig.getUpdate().setMessage("Updates are now enabled");

        assertEquals(false, appConfig.getUpdate().isDisabled());
        assertEquals("Updates are now enabled", appConfig.getUpdate().getMessage());
    }
}

