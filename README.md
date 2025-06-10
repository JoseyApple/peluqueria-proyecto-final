# GlowUpStudio - Backend

Backend para la gestión de una peluquería, desarrollado en Java con Spring Boot. Permite gestionar usuarios, citas, servicios y facturas, con autenticación basada en JWT.

## Requisitos

- Java 17 o superior
- Maven 3.8+
- MySQL Server
- (Opcional) H2 para pruebas locales
- Postman o Swagger para pruebas de endpoints

---

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/tuusuario/glowupstudio-backend.git
cd glowupstudio-backend
```

### 2. Configurar base de datos

Crea una base de datos en MySQL:

```sql
CREATE DATABASE glowupstudio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Editar archivo `application.yml`

Ubica o crea el archivo `src/main/resources/application.yml` con el siguiente contenido:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/glowupstudio
    username: tu_usuario
    password: tu_contraseña
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect

security:
  jwt:
    secret-key: c5cce0a3980ae936bbb7ec9c526f4057043c28b0da0bbb5caca7b713ad2de9df
    expiration-time: 3600000
```

### 4. Construir el proyecto

```bash
mvn clean install
```

### 5. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

> Por defecto, el backend se ejecuta en `http://localhost:8081`.

---

## Herramientas disponibles

- **Swagger UI**: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- **H2 Console** (solo si activas H2): [http://localhost:8081/h2-console](http://localhost:8081/h2-console)

---

## Seguridad y autenticación

La autenticación se realiza mediante JWT. Se proporcionan los siguientes endpoints públicos:

- `POST /auth/login` - Autenticación
- `POST /users` - Registro de nuevo usuario

Tras autenticarse, se debe incluir el token en las peticiones protegidas con:

```http
Authorization: Bearer <token>
```

---

## Notas

- Al crear una cita, se genera automáticamente una factura (`Order`) asociada.
- Las citas se marcan automáticamente como expiradas si no se confirman antes de su horario.
