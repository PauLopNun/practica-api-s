package com.exampleinyection.clase2parte2.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Max;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "app")
@Validated
public class AppConfig {

    @NotBlank(message = "El nombre de la app no puede estar vacío")
    private String name;

    @Pattern(regexp = "DEV|PROD|LOCAL|TEST", message = "El entorno debe ser DEV, PROD, LOCAL o TEST")
    private String environment = "LOCAL";

    @Min(1)
    @Max(100)
    private int poolSize = 10;

    @NotNull @Valid
    private DatabaseSettings database = new DatabaseSettings();

    @NotNull @Valid
    private CommonSettings common = new CommonSettings();

    @NotNull @Valid
    private UpdateSettings update = new UpdateSettings();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public DatabaseSettings getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseSettings database) {
        this.database = database;
    }

    public CommonSettings getCommon() {
        return common;
    }

    public void setCommon(CommonSettings common) {
        this.common = common;
    }

    public UpdateSettings getUpdate() {
        return update;
    }

    public void setUpdate(UpdateSettings update) {
        this.update = update;
    }

    public static class DatabaseSettings {
        @NotBlank(message = "La contraseña no puede estar vacía")
        private String password;

        private String url;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class CommonSettings {
        @NotNull @Valid
        private PaginationSettings pagination = new PaginationSettings();

        @NotNull @Valid
        private DefaultSettings defaults = new DefaultSettings();

        public PaginationSettings getPagination() {
            return pagination;
        }

        public void setPagination(PaginationSettings pagination) {
            this.pagination = pagination;
        }

        public DefaultSettings getDefaults() {
            return defaults;
        }

        public void setDefaults(DefaultSettings defaults) {
            this.defaults = defaults;
        }
    }

    public static class UpdateSettings {
        private boolean disabled;
        @NotBlank
        private String message = "";

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class PaginationSettings {
        @Min(1)
        @Max(1000)
        private int maxSize = 10;

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }
    }

    public static class DefaultSettings {
        @NotBlank
        private String name = "";
        @Min(0)
        private int age = 0;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}