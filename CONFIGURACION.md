# Configuración de la Aplicación GFTFormacion

## Estado Actual

La aplicación ha sido configurada correctamente con:

### 1. Perfiles de Spring (Dev, Local, Prod)
   - Perfil local activo por defecto (configurado en application.yml)
   - Perfiles separados en archivos YAML:
     - application-local.yml
     - application-dev.yml
     - application-prod.yml

### 2. Propiedades Sensibles con Variables de Entorno
   - Propiedad password añadida a AppConfig
   - Configurada para usar variables de entorno: ${DB_PASSWORD:valor_por_defecto}
   - En producción, la contraseña NUNCA debe aparecer en los archivos de configuración

### 3. Validación @NotNull y @Validated
   - @Validated añadido a AppConfig
   - @NotNull @Valid en propiedades de configuración
   - Valores por defecto inicializados para evitar errores de validación

### 4. Tests Unitarios
   - Todos los tests pasan: 41 tests, 0 failures
   - Incluyen pruebas de la propiedad password

---

## Cómo Ejecutar

### Ejecutar con Perfil Local (por defecto)
```bash
.\mvnw.cmd spring-boot:run
```

Verás en los logs:
```
The following 1 profile is active: "local"
App name: GFTSpringBoot
```

### Ejecutar con Perfil Dev
```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Ejecutar con Perfil Prod
```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### Ejecutar con Variable de Entorno (Contraseña)

#### En PowerShell:
```powershell
$env:DB_PASSWORD="mi_contraseña_super_secreta"
.\mvnw.cmd spring-boot:run
```

#### En CMD (Windows):
```cmd
set DB_PASSWORD=mi_contraseña_super_secreta
mvnw.cmd spring-boot:run
```

#### En Linux/Mac:
```bash
export DB_PASSWORD="mi_contraseña_super_secreta"
./mvnw spring-boot:run
```

---

## Valores de Configuración por Perfil

### Local
```yaml
app:
  name: "Mi App GFT - LOCAL"
  password: ${DB_PASSWORD:default_local_password}  # Valor por defecto si no se especifica
  update:
    disabled: false
    message: "No se puede actualizar ahora mismo"
  pagination:
    max-size: 50
  defaults:
    name: "Usuario Anónimo"
    age: 18
```

### Dev
```yaml
app:
  name: "GFT - Entorno DESARROLLO"
  password: ${DB_PASSWORD:dev_password_123}
  update:
    disabled: true
    message: "Update bloqueado en DEV por mantenimiento"
  pagination:
    max-size: 20
  defaults:
    name: "User_Dev"
    age: 25
```

### Prod
```yaml
app:
  name: "Mi App GFT REAL"
  password: ${DB_PASSWORD:prod_password_securely_set}
  update:
    disabled: true
    message: "¡PELIGRO! No se puede actualizar en PRODUCCIÓN"
  pagination:
    max-size: 100
  defaults:
    name: "Usuario_Anonimo"
    age: 18
```

---

## Buenas Prácticas de Seguridad

### Lo que Hacemos Bien
- Las contraseñas **NO** están en los archivos YAML
- Usamos variables de entorno para propiedades sensibles
- En producción, la variable `DB_PASSWORD` debe establecerse de forma segura
- Los archivos YAML tienen valores por defecto solo para desarrollo local

### Para Producción
- **NUNCA** dejes la contraseña por defecto en el archivo YAML
- Usa un gestor de secretos (AWS Secrets Manager, HashiCorp Vault, etc.)
- Configura las variables de entorno de forma segura en tu infraestructura
- En Docker: usa `docker run -e DB_PASSWORD=xxx`
- En Kubernetes: usa Secrets

---

## Ejecutar Tests

```bash
.\mvnw.cmd test
```

Resultado esperado:
```
[INFO] Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Archivos Modificados

1. **`src/main/java/com/exampleinyection/clase2parte2/config/AppConfig.java`**
   - Añadida propiedad `password` con `@NotBlank`
   - Añadidos getters y setters
   - Inicializados valores por defecto

2. **`src/main/resources/application.yml`**
   Perfil por defecto: `local`

3. **`src/main/resources/application-local.yml`**
4. **`src/main/resources/application-dev.yml`**
5. **`src/main/resources/application-prod.yml`**
   - Añadida propiedad `password: ${DB_PASSWORD:valor_defecto}`

6. **`src/test/java/com/exampleinyection/clase2parte2/config/AppConfigTest.java`**
   - Añadido test para la propiedad `password`

---

## Resumen

Todo está correctamente configurado:
- Perfiles de Spring funcionando
- Validación de propiedades con `@NotNull` y `@Validated`
- Propiedades sensibles usando variables de entorno
- 41 tests pasando
- Aplicación iniciando correctamente

