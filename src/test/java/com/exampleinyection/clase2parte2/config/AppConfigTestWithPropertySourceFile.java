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
    "app.name=Integration Test Configuration",
    "app.database.password=file_based_password",
    "app.update.disabled=false",
    "app.update.message=Updates enabled in test",
    "app.common.pagination.max-size=15",
    "app.common.defaults.name=File-based Default",
    "app.common.defaults.age=19"
})
public class AppConfigTestWithPropertySourceFile {

    @Autowired
    private AppConfig appConfig;

    @Test
    void testAppConfigWithPropertiesFromFile() {
        assertEquals("Integration Test Configuration", appConfig.getName());
        assertEquals("file_based_password", appConfig.getDatabase().getPassword());
        assertEquals(false, appConfig.getUpdate().isDisabled());
        assertEquals(15, appConfig.getCommon().getPagination().getMaxSize());
        assertEquals("File-based Default", appConfig.getCommon().getDefaults().getName());
        assertEquals(19, appConfig.getCommon().getDefaults().getAge());
    }

    @Test
    void testPasswordFromPropertyFile() {
        assertEquals("file_based_password", appConfig.getDatabase().getPassword());
    }

    @Test
    void testPaginationFromPropertyFile() {
        int maxSize = appConfig.getCommon().getPagination().getMaxSize();
        assertEquals(15, maxSize);
    }
}

