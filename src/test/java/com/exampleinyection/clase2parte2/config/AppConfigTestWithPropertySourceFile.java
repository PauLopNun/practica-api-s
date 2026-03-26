package com.exampleinyection.clase2parte2.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {
    "app.name=Integration Test Configuration",
    "app.password=file_based_password",
    "app.update.disabled=false",
    "app.update.message=Updates enabled in test",
    "app.pagination.max-size=15",
    "app.defaults.name=File-based Default",
    "app.defaults.age=19"
})
public class AppConfigTestWithPropertySourceFile {

    @Autowired
    private AppConfig appConfig;

    @Test
    void testAppConfigWithPropertiesFromFile() {
        assertEquals("Integration Test Configuration", appConfig.getName());
        assertEquals("file_based_password", appConfig.getPassword());
        assertEquals(false, appConfig.getUpdate().isDisabled());
        assertEquals(15, appConfig.getPagination().getMaxSize());
        assertEquals("File-based Default", appConfig.getDefaults().getName());
        assertEquals(19, appConfig.getDefaults().getAge());
    }

    @Test
    void testPasswordFromPropertyFile() {
        assertEquals("file_based_password", appConfig.getPassword());
    }

    @Test
    void testPaginationFromPropertyFile() {
        int maxSize = appConfig.getPagination().getMaxSize();
        assertEquals(15, maxSize);
    }
}

