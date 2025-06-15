# Glow Up Studio - Backend

Backend para la gestión de una peluquería, desarrollado en **Java con Spring Boot**. Este sistema permite gestionar usuarios, citas, servicios y facturas, con autenticación basada en **JWT**.

https://glowupstudio-main.netlify.app/

https://peluqueria-proyecto-final.onrender.com/swagger-ui/index.html

---

## 🛠️ Tecnologías Utilizadas

- Java 17+
- Spring Boot
- MySQL / H2 (opcional para pruebas)
- Maven 3.8+
- JWT para autenticación
- Swagger para documentación de API

---

## 🚀 Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/tuusuario/glowupstudio-backend.git
cd glowupstudio-backend
```

### 2. Crear base de datos en MySQL

```sql
CREATE DATABASE glowupstudio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configurar archivo `application.yml`

Ubicado en `src/main/resources/application.yml`:

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

> 📍 Por defecto, el backend se ejecuta en: `http://localhost:8081`

---

## 🔐 Seguridad y Autenticación

El backend utiliza JWT para autenticar y autorizar peticiones.

### Endpoints públicos

- `POST /auth/login` - Iniciar sesión
- `POST /users` - Registro de nuevo usuario

### Endpoints protegidos

Incluir el token JWT en la cabecera de autorización:

```http
Authorization: Bearer <token>
```

---

## 🧪 Herramientas para pruebas

- **Swagger UI**: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- **H2 Console** (si está activado): [http://localhost:8081/h2-console](http://localhost:8081/h2-console)

---

## 🧾 Notas Importantes

- Al crear una cita, se genera automáticamente una factura (`Order`) asociada.
- Las citas se marcan como expiradas si no se confirman antes de su horario.

---

## 🌐 Proyecto Frontend

El frontend está desarrollado en **Vue 3**, utilizando **Pinia**, **Vue Router** y **Axios**.

### Principales vistas

- **Inicio**: Página principal con carrusel e información de servicios.
- **Servicios**: Listado y detalle de servicios disponibles.
- **Reservas**: Gestión de reservas del usuario.
- **Administración**: Panel de administración (solo para admins).
- **Login / Registro**: Autenticación de usuarios.
- **Factura**: Visualización y descarga en PDF.
- **Sobre Nosotros / Contacto**: Información institucional.

### Tecnologías utilizadas

- Vue 3 (Composition API)
- Pinia (estado global)
- Vue Router
- Axios
- jsPDF (generación de PDFs)
- Swiper (carrusel)
- Vite

### Instalación del frontend

```bash
git clone https://github.com/tu-usuario/glow-up-studio.git
cd glow-up-studio/frontend
npm install
npm run dev
```

> 🔗 Por defecto, el frontend corre en: `http://localhost:5173`

### Producción

```bash
npm run build
```

Sube el contenido de la carpeta `dist/` a tu hosting (Netlify, Vercel, etc). Asegúrate de actualizar la URL del backend en `src/api/axiosInstance.js`.

---

## 📩 Contacto

Para soporte o contribuciones, por favor abre un issue o pull request en el repositorio.

---

© 2025 Glow Up Studio

