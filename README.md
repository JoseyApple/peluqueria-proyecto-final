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

Glow Up Studio - Frontend
----PROYECTO DE VUE 3 + PINIA + VUE ROUTER + AXIOS----

Vistas principales:
Inicio: Página principal con carrusel y resumen de servicios.

Servicios: Listado de todos los servicios con detalle.

Detalle de Servicio: Información específica y opción para reservar.

Reservas: Gestión y visualización de reservas del usuario.

Administración: Panel para gestión de reservas y facturas (solo admin).

Login / Registro: Autenticación de usuarios.

Factura: Visualización y descarga de facturas.

Sobre Nosotros y Contacto: Información corporativa y contacto.

Descripción del Proyecto
Glow Up Studio es una aplicación web para gestión y reserva de servicios de belleza y peluquería. Ofrece a los usuarios una experiencia moderna y responsive para consultar servicios, realizar reservas y gestionar facturas. La aplicación está dividida en frontend (Vue 3) y backend (Node.js + Express).

Estructura del Proyecto
Glow Up Studio
│
├── 📂 frontend
│ ├── 📂 public
│ ├── 📂 src
│ │ ├── 📂 api (configuración axios)
│ │ ├── 📂 components (componentes reutilizables)
│ │ ├── 📂 plugins (modal, etc.)
│ │ ├── 📂 stores (Pinia para estado global)
│ │ ├── 📂 views (vistas / páginas)
│ │ ├── App.vue
│ │ ├── main.js
│ │ ├── router.js
│ ├── package.json
│ ├── vite.config.js
│
└── 📜 .gitignore

Tecnologías Utilizadas
Frontend

Vue 3 (Composición API)

Pinia (Gestión de estado)

Vue Router (Navegación)

Axios (Comunicación con backend)

Swiper (Carrusel de imágenes)

jsPDF (Generación de PDFs para facturas)

Vite (Empaquetador y servidor de desarrollo)

Instalación y Configuración
Clonar el repositorio

git clone https://github.com/tu-usuario/glow-up-studio.git
cd glow-up-studio/frontend
Instalar dependencias

npm install
Configurar backend
Asegúrate de que el backend esté corriendo en http://localhost:8081 (puedes ajustar la URL en src/api/axiosInstance.js si es necesario).

Ejecutar la aplicación

npm run dev
La app estará disponible usualmente en http://localhost:5173.

Uso básico
Regístrate o inicia sesión para reservar servicios.

Navega en la sección de servicios para conocer las opciones disponibles.

Reserva citas con selección de fecha y hora, el sistema verifica disponibilidad.

Visualiza y administra tus reservas y facturas en tu perfil.

Los administradores pueden gestionar todas las reservas y facturas desde el panel de administración.

Despliegue
Recomendaciones para producción
Construir la app para producción:

npm run build

Subir los archivos generados en dist/ a tu proveedor de hosting estático (Netlify, Vercel, etc).

Actualizar la URL del backend en src/api/axiosInstance.js si el backend se despliega en otra URL pública.

Notas
El sistema usa JWT para autenticación. El token se almacena en localStorage.

Algunas operaciones (como confirmaciones y cancelaciones) requieren rol de administrador.

El plugin modal centraliza todas las alertas, confirmaciones y prompts para mejor experiencia.

## Notas

- Al crear una cita, se genera automáticamente una factura (`Order`) asociada.
- Las citas se marcan automáticamente como expiradas si no se confirman antes de su horario.
