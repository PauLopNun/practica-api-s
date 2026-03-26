package com.exampleinyection.clase2parte2.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext
@TestPropertySource(properties = {
    "spring.profiles.active=",
    "app.name=Test Application Custom Name",
    "app.database.password=test_password_123",
    "app.update.disabled=true",
    "app.update.message=Updates disabled for testing",
    "app.common.pagination.max-size=25",
    "app.common.defaults.name=Default Test User",
    "app.common.defaults.age=21"
})
public class AppConfigTestWithPropertySource {

    @Autowired
    private AppConfig appConfig;

    @Test
    void testAppConfigWithCustomProperties() {
        assertEquals("Test Application Custom Name", appConfig.getName());
        assertEquals("test_password_123", appConfig.getDatabase().getPassword());
        assertEquals(true, appConfig.getUpdate().isDisabled());
        assertEquals("Updates disabled for testing", appConfig.getUpdate().getMessage());
        assertEquals(25, appConfig.getCommon().getPagination().getMaxSize());
        assertEquals("Default Test User", appConfig.getCommon().getDefaults().getName());
        assertEquals(21, appConfig.getCommon().getDefaults().getAge());
    }

    @Test
    void testAppConfigPropertiesCanBeOverridden() {
        String originalName = appConfig.getName();
        String originalPassword = appConfig.getDatabase().getPassword();
        
        assertEquals("Test Application Custom Name", originalName);
        assertEquals("test_password_123", originalPassword);

        appConfig.setName("Updated Name");
        appConfig.getDatabase().setPassword("updated_password");

        assertEquals("Updated Name", appConfig.getName());
        assertEquals("updated_password", appConfig.getDatabase().getPassword());
        
        appConfig.setName(originalName);
        appConfig.getDatabase().setPassword(originalPassword);
    }

    @Test
    void testAppConfigPaginationSettings() {
        int originalMaxSize = appConfig.getCommon().getPagination().getMaxSize();
        assertEquals(25, originalMaxSize);

        appConfig.getCommon().getPagination().setMaxSize(50);
        assertEquals(50, appConfig.getCommon().getPagination().getMaxSize());
        
        appConfig.getCommon().getPagination().setMaxSize(originalMaxSize);
    }

    @Test
    void testAppConfigDefaultSettings() {
        String originalDefaultName = appConfig.getCommon().getDefaults().getName();
        int originalDefaultAge = appConfig.getCommon().getDefaults().getAge();
        
        assertEquals("Default Test User", originalDefaultName);
        assertEquals(21, originalDefaultAge);

        appConfig.getCommon().getDefaults().setName("Different Default");
        appConfig.getCommon().getDefaults().setAge(30);

        assertEquals("Different Default", appConfig.getCommon().getDefaults().getName());
        assertEquals(30, appConfig.getCommon().getDefaults().getAge());
        
        appConfig.getCommon().getDefaults().setName(originalDefaultName);
        appConfig.getCommon().getDefaults().setAge(originalDefaultAge);
    }

    @Test
    void testAppConfigUpdateSettings() {
        boolean originalDisabled = appConfig.getUpdate().isDisabled();
        String originalMessage = appConfig.getUpdate().getMessage();
        
        assertEquals(true, originalDisabled);
        assertEquals("Updates disabled for testing", originalMessage);

        appConfig.getUpdate().setDisabled(false);
        appConfig.getUpdate().setMessage("Updates are now enabled");

        assertEquals(false, appConfig.getUpdate().isDisabled());
        assertEquals("Updates are now enabled", appConfig.getUpdate().getMessage());
        
        appConfig.getUpdate().setDisabled(originalDisabled);
        appConfig.getUpdate().setMessage(originalMessage);
    }
}

