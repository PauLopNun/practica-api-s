@echo off
REM Script de demostración: Perfiles de Spring Boot y variables de entorno

echo.
echo ================================================
echo Demostración: Perfiles y Configuracion Spring Boot
echo ================================================
echo.

echo 1. PERFIL LOCAL (por defecto)
echo    Password: default_local_password
echo    Comando: mvnw.cmd spring-boot:run
echo.

echo 2. PERFIL DEV
echo    Password: dev_password_123
echo    Comando: mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
echo.

echo 3. PERFIL PROD
echo    Password: prod_password_securely_set
echo    Comando: mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
echo.

echo 4. CON VARIABLE DE ENTORNO PERSONALIZADA
echo    Comandos:
echo    set DB_PASSWORD=mi_contraseña_super_secreta
echo    mvnw.cmd spring-boot:run
echo.

echo ================================================
echo EJECUTAR TESTS
echo ================================================
echo.
echo Comando: mvnw.cmd test
echo.

echo ================================================
echo GENERAR JAR
echo ================================================
echo.
echo Comando: mvnw.cmd clean package
echo.

echo ================================================
echo Ver detalles en README.md y CONFIGURACION.md
echo ================================================
echo.



