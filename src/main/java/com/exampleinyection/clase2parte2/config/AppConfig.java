package com.exampleinyection.clase2parte2.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "app")
@Validated
@Getter @Setter
public class AppConfig {

    @NotBlank(message = "El nombre de la app no puede estar vacío")
    private String name;

    @Pattern(regexp = "DEV|PROD|LOCAL|TEST", message = "El entorno debe ser DEV, PROD, LOCAL o TEST")
    private String environment = "LOCAL";

    @Min(1) @Max(100)
    private int poolSize = 10;

    @NotNull @Valid
    private DatabaseSettings database = new DatabaseSettings();

    @NotNull @Valid
    private CommonSettings common = new CommonSettings();

    @NotNull @Valid
    private UpdateSettings update = new UpdateSettings();

    @Getter @Setter
    public static class DatabaseSettings {
        @NotBlank(message = "La contraseña no puede estar vacía")
        private String password;
        private String url;
    }

    @Getter @Setter
    public static class CommonSettings {
        @NotNull @Valid
        private PaginationSettings pagination = new PaginationSettings();
        @NotNull
        @Valid
        private DefaultSettings defaults = new DefaultSettings();
    }

    @Getter
    @Setter
    public static class UpdateSettings {
        private boolean disabled;
        @NotBlank
        private String message = "";
    }

    @Getter @Setter
    public static class PaginationSettings {
        @Min(1) @Max(1000)
        private int maxSize = 10;
    }

    @Getter @Setter
    public static class DefaultSettings {
        @NotBlank
        private String name = "";
        @Min(0)
        private int age = 0;
    }
}