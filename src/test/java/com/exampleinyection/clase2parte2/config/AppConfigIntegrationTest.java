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
    "app.name=Testing Environment Configuration",
    "app.environment=TEST",
    "app.pool-size=15",
    "app.database.password=${TEST_PASSWORD:default_test_password}",
    "app.database.url=jdbc:h2:mem:testdb",
    "app.update.disabled=false",
    "app.update.message=Testing allowed",
    "app.common.pagination.max-size=30",
    "app.common.defaults.name=Test Default User",
    "app.common.defaults.age=25"
})
public class AppConfigIntegrationTest {

    @Autowired
    private AppConfig appConfig;

    @Test
    void testCompleteAppConfigurationForTesting() {
        assertEquals("Testing Environment Configuration", appConfig.getName());
        assertEquals("TEST", appConfig.getEnvironment());
        assertEquals(15, appConfig.getPoolSize());
        assertEquals("default_test_password", appConfig.getDatabase().getPassword());
        assertEquals("jdbc:h2:mem:testdb", appConfig.getDatabase().getUrl());
        assertEquals(false, appConfig.getUpdate().isDisabled());
        assertEquals("Testing allowed", appConfig.getUpdate().getMessage());
        assertEquals(30, appConfig.getCommon().getPagination().getMaxSize());
        assertEquals("Test Default User", appConfig.getCommon().getDefaults().getName());
        assertEquals(25, appConfig.getCommon().getDefaults().getAge());
    }

    @Test
    void testPasswordCanUseEnvironmentVariable() {
        String password = appConfig.getDatabase().getPassword();
        assertEquals("default_test_password", password);
    }

    @Test
    void testUpdateSettingsAreMutable() {
        assertEquals(false, appConfig.getUpdate().isDisabled());

        AppConfig.UpdateSettings tempUpdateSettings = new AppConfig.UpdateSettings();
        tempUpdateSettings.setDisabled(true);
        assertEquals(true, tempUpdateSettings.isDisabled());

        tempUpdateSettings.setDisabled(false);
        assertEquals(false, tempUpdateSettings.isDisabled());

        assertEquals(false, appConfig.getUpdate().isDisabled());
    }

    @Test
    void testPaginationSettingsCanBeModified() {
        assertEquals(30, appConfig.getCommon().getPagination().getMaxSize());

        AppConfig.PaginationSettings tempPagination = new AppConfig.PaginationSettings();
        tempPagination.setMaxSize(100);
        assertEquals(100, tempPagination.getMaxSize());

        assertEquals(30, appConfig.getCommon().getPagination().getMaxSize());
    }

    @Test
    void testDefaultSettingsCanBeCustomized() {
        assertEquals("Test Default User", appConfig.getCommon().getDefaults().getName());
        assertEquals(25, appConfig.getCommon().getDefaults().getAge());

        AppConfig.DefaultSettings customDefaults = new AppConfig.DefaultSettings();
        customDefaults.setName("Custom User");
        customDefaults.setAge(28);

        assertEquals("Custom User", customDefaults.getName());
        assertEquals(28, customDefaults.getAge());

        assertEquals("Test Default User", appConfig.getCommon().getDefaults().getName());
        assertEquals(25, appConfig.getCommon().getDefaults().getAge());
    }

    @Test
    void testValidationWorks() {
        String originalName = appConfig.getName();
        assertEquals("Testing Environment Configuration", originalName);

        AppConfig.DefaultSettings testDefaults = new AppConfig.DefaultSettings();
        testDefaults.setName("Test Name");
        assertEquals("Test Name", testDefaults.getName());
    }
}
