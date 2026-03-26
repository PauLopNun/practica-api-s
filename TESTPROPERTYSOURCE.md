# TestPropertySource - Guía de Uso

## Qué es @TestPropertySource

@TestPropertySource es una anotación de Spring que permite sobrescribir propiedades de configuración durante los tests, sin afectar la configuración de producción. Es muy útil para:

- Ejecutar tests con configuraciones específicas
- Usar variables de entorno en tests
- Probar diferentes valores de configuración
- Aislar tests de la configuración global

## Formas de Uso

### 1. Usando properties (Inline)

La forma más simple es pasar las propiedades directamente como array de strings:

```java
@SpringBootTest
@TestPropertySource(properties = {
    "app.name=Test Configuration",
    "app.password=test_password_123",
    "app.pagination.max-size=25"
})
public class MyConfigTest {
    @Autowired
    private AppConfig appConfig;

    @Test
    void testWithCustomProperties() {
        assertEquals("Test Configuration", appConfig.getName());
        assertEquals("test_password_123", appConfig.getPassword());
        assertEquals(25, appConfig.getPagination().getMaxSize());
    }
}
```

**Ventajas:**
- Simple y directo
- Visible en el código
- No requiere archivos adicionales

**Desventajas:**
- Puede ser verboso para muchas propiedades
- Menos legible con muchos parámetros

### 2. Usando locations (Archivo Externo)

Puedes referenciar un archivo de propiedades externo. Nota: Funciona mejor con .properties que con .yml

```java
@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
public class MyConfigFileTest {
    @Autowired
    private AppConfig appConfig;

    @Test
    void testWithPropertiesFile() {
        assertEquals("File Configuration", appConfig.getName());
    }
}
```

Archivo test-application.properties:
```properties
app.name=File Configuration
app.password=file_password
app.pagination.max-size=15
```

**Ventajas:**
- Configuración centralizada
- Más fácil de mantener
- Reutilizable en múltiples tests

**Desventajas:**
- Requiere archivo adicional
- Menos visible en el código

### 3. Combinando properties y variables de entorno

```java
@SpringBootTest
@TestPropertySource(properties = {
    "app.name=Test App",
    "app.password=${DB_PASSWORD:default_test_password}",
    "app.pagination.max-size=30"
})
public class AppConfigIntegrationTest {
    @Autowired
    private AppConfig appConfig;

    @Test
    void testPasswordFromEnvironment() {
        assertEquals("default_test_password", appConfig.getPassword());
    }
}
```

Si ejecutas el test con una variable de entorno:
```bash
export DB_PASSWORD="custom_password"
mvnw test
```

El valor será "custom_password" en lugar de "default_test_password".

## Ejemplo Completo: Probando AppConfig

```java
@SpringBootTest
@TestPropertySource(properties = {
    "app.name=Testing Environment Configuration",
    "app.password=test_password_123",
    "app.update.disabled=false",
    "app.update.message=Testing allowed",
    "app.pagination.max-size=30",
    "app.defaults.name=Test Default User",
    "app.defaults.age=25"
})
public class AppConfigTestWithPropertySource {

    @Autowired
    private AppConfig appConfig;

    @Test
    void testAllPropertiesLoaded() {
        assertEquals("Testing Environment Configuration", appConfig.getName());
        assertEquals("test_password_123", appConfig.getPassword());
        assertEquals(false, appConfig.getUpdate().isDisabled());
        assertEquals("Testing allowed", appConfig.getUpdate().getMessage());
        assertEquals(30, appConfig.getPagination().getMaxSize());
        assertEquals("Test Default User", appConfig.getDefaults().getName());
        assertEquals(25, appConfig.getDefaults().getAge());
    }

    @Test
    void testConfigurationCanBeModified() {
        String originalName = appConfig.getName();
        assertEquals("Testing Environment Configuration", originalName);

        AppConfig.DefaultSettings newDefaults = new AppConfig.DefaultSettings();
        newDefaults.setName("Different Name");
        assertEquals("Different Name", newDefaults.getName());
    }
}
```

## Orden de Carga de Propiedades

Spring carga las propiedades en este orden (última gana):
1. application.properties / application.yml
2. application-{perfil}.properties / application-{perfil}.yml
3. @TestPropertySource (properties)
4. @TestPropertySource (locations)
5. Variables de entorno del sistema

## Mejores Prácticas

### 1. No Modifiques el Bean Compartido en Tests

INCORRECTO:
```java
@Test
void testBadPractice() {
    appConfig.setName("Modified");
    assertEquals("Modified", appConfig.getName());
    // El bean quedó modificado para los siguientes tests
}
```

CORRECTO:
```java
@Test
void testGoodPractice() {
    AppConfig tempConfig = new AppConfig();
    tempConfig.setName("Modified");
    assertEquals("Modified", tempConfig.getName());
    // El bean original no fue modificado
}
```

### 2. Usa Nombres Descriptivos

```java
@TestPropertySource(properties = {
    "app.name=Test Configuration For User Service",
    "app.pagination.max-size=25"
})
public class UserServiceConfigTest {
    // ...
}
```

### 3. Documenta la Intención del Test

```java
@TestPropertySource(properties = {
    "app.update.disabled=true"
})
public class UpdateDisabledTest {
    @Test
    void testThatUpdatesAreBlockedWhenDisabled() {
        // Prueba que los updates no funcionan cuando disabled=true
    }
}
```

## Comparación: @TestPropertySource vs Perfiles

### Usando Perfil de Test

```java
@SpringBootTest(profiles = "test")
public class ConfigTestWithProfile {
    // Carga application-test.yml
}
```

### Usando @TestPropertySource

```java
@SpringBootTest
@TestPropertySource(properties = {
    "app.name=Test Config"
})
public class ConfigTestWithProperties {
    // Carga propiedades sobrescritas
}
```

**Diferencia:**
- Perfiles: Para toda una clase de configuración
- @TestPropertySource: Para propiedades específicas por test

## Casos de Uso Reales

### 1. Testear Configuración de Diferentes Entornos

```java
@SpringBootTest
@TestPropertySource(properties = {
    "app.name=Local Test Environment",
    "app.pagination.max-size=10"
})
public class LocalEnvironmentTest { }

@SpringBootTest
@TestPropertySource(properties = {
    "app.name=Production Test Environment",
    "app.pagination.max-size=100"
})
public class ProdEnvironmentTest { }
```

### 2. Testear Manejo de Contraseñas desde Variables de Entorno

```java
@SpringBootTest
@TestPropertySource(properties = {
    "app.password=${API_KEY:default_key_for_testing}"
})
public class PasswordSecurityTest {
    @Test
    void testPasswordIsLoaded() {
        assertNotNull(appConfig.getPassword());
    }
}
```

### 3. Testear Diferentes Configuraciones de Actualización

```java
@SpringBootTest
@TestPropertySource(properties = {
    "app.update.disabled=true",
    "app.update.message=Updates disabled"
})
public class UpdateDisabledTest {
    @Test
    void testUpdateThrowsException() {
        // Verifica que los updates son bloqueados
    }
}

@SpringBootTest
@TestPropertySource(properties = {
    "app.update.disabled=false",
    "app.update.message=Updates enabled"
})
public class UpdateEnabledTest {
    @Test
    void testUpdateWorks() {
        // Verifica que los updates funcionan
    }
}
```

## Resumen

@TestPropertySource es una herramienta poderosa para:
- Aislar tests con configuraciones específicas
- Probar diferentes escenarios de configuración
- Validar que la aplicación funciona con diferentes valores
- Manejar propiedades sensibles de forma segura en tests

Use properties para valores simples y locations para configuraciones más complejas.

