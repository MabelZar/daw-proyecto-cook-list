# 🍽️ Cook-List

Aplicación web para planificar las comidas semanales del hogar, organizar platos e ingredientes y generar una lista de la compra adaptada al número de personas.

## 👤 Información del Alumno

- **Autor:** Mabel Susan Zárate Espíritu
- **Asignatura:** Proyecto Intermodular
- **Curso / Grupo:** 2026/2027 — 2.º DAW
- **Centro:** CIFP Avilés
- **Hito:** 2 (Sprint 2)
- **Proyecto:** Cook-List
- **Profesor/a:** Pablo Lera Menendez

---

## 🚀 Comenzando

Cook-List se encuentra en la fase inicial del proyecto. Las instrucciones de instalación y ejecución se completarán cuando esté preparada la estructura de la aplicación.

### 📋 Prerrequisitos

Tecnologías previstas para el desarrollo:

- Java y Spring Boot para el backend.
- React.js, HTML, CSS y JavaScript para el frontend.
- PostgreSQL como base de datos.
- Maven para gestionar las dependencias del backend.
- Git y GitHub para el control de versiones.

### 🔧 Instalación y Despliegue

Los pasos de instalación, configuración y ejecución se documentarán cuando esté implementada la primera versión funcional.

### ⚙️ Configuración: fichero `.env`

Antes de ejecutar el servicio hay que crear el fichero `.env` en la raíz del proyecto a partir de la plantilla `.env.example`, ya que la aplicación carga sus variables al arrancar (`spring.config.import` en `application.properties`):

```powershell
Copy-Item .env.example .env
```

Después, edita `.env` y completa los valores de conexión a PostgreSQL (`DB_USERNAME`, `DB_PASSWORD`), el secreto JWT (`JWT_SECRET`) y las contraseñas de los usuarios iniciales (`ADMIN_PASSWORD`, `USER_PASSWORD`). El fichero `.env` está excluido de Git mediante `.gitignore` para no subir secretos.

### ▶️ Inicio del servicio (Windows)

Modo normal:

```powershell
.\mvnw spring-boot:run
```

Modo debug (queda a la escucha en el puerto 5005 para conectar el depurador):

```powershell
.\mvnw spring-boot:run "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
```

### ♻️ Regeneración de la persistencia desde cero (comando único)

Para reconstruir la base de datos desde cero existe un único comando:

```bat
.\regenerar-bd.bat
```

El script:

1. Lee la configuración de acceso a PostgreSQL desde el archivo `.env` (variables `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`).
2. Elimina la base de datos `cook_list` (con `DROP DATABASE ... WITH (FORCE)`, cerrando conexiones activas) y la vuelve a crear vacía.
3. La aplicación se inicia con `mvnw spring-boot:run`: **Flyway** detecta la BD vacía y aplica las migraciones en orden:
   - `V1__create_initial_schema.sql` — esquema (roles, users, categories, units, ingredients, recipes…).
   - `V2__insert_initial_data.sql` — roles, extensión `pgcrypto` y usuarios iniciales (contraseñas hasheadas con BCrypt).
   - `V3__allow_public_recipes.sql` — recetas públicas y extensión `unaccent`.
   - `V4__insert_initial_recipes.sql` — recetas de prueba.

**Requisitos:** PostgreSQL arrancado y `psql` disponible (si no está en el PATH, el script lo busca en la instalación típica `C:\Program Files\PostgreSQL\<versión>\bin`). Requiere `psql` versión 13 o superior (por `WITH (FORCE)`).

---

## 🛠️ Tecnologías Utilizadas

- **Backend:** Java y Spring Boot.
- **Frontend:** React.js.
- **Lenguajes web:** HTML, CSS y JavaScript.
- **Base de datos:** PostgreSQL.
- **Arquitectura:** MVC en tres capas.
- **Acceso a datos:** Spring Data JPA / Hibernate.
- **Gestión de dependencias:** Maven.
- **Pruebas de API:** Postman.
- **Entorno de desarrollo:** Visual Studio Code.
- **Despliegue:** Railway.

---

## 📖 Decisiones de Diseño y Requisitos Cumplidos

### Objetivo del proyecto

Cook-List facilitará la planificación semanal de comidas y la organización de la compra doméstica. Permitirá registrar usuarios, gestionar platos e ingredientes, organizar menús semanales y generar listas de la compra con cantidades adaptadas al número de personas.

### Público objetivo

Personas, familias y hogares que quieran organizar sus comidas y preparar la compra semanal de forma más eficiente.

### Funcionalidades previstas para el MVP

- Registro, autenticación y gestión básica del perfil.
- Creación, edición, eliminación y búsqueda de platos.
- Gestión de ingredientes, cantidades y unidades de medida.
- Planificación y modificación de menús semanales.
- Cálculo de cantidades según el número de personas.
- Generación y gestión de listas de la compra.

### Roles de usuario

- **Público o anónimo:** consultar información general, registrarse e iniciar sesión.
- **Usuario registrado:** gestionar su perfil, platos, menús y listas de la compra.
- **Administrador:** consultar cuentas registradas, desactivar usuarios y consultar métricas generales.

### Decisiones técnicas

La aplicación utilizará React.js para la interfaz web, Spring Boot para desarrollar una API REST y PostgreSQL para almacenar los datos. La arquitectura MVC en tres capas separará controladores, servicios y acceso a datos.

---

## 🔍 Pruebas y Validación

Las pruebas de los endpoints, las validaciones y las respuestas HTTP se realizarán mediante Postman. Los procedimientos de prueba se documentarán conforme se implementen las funcionalidades.

---

## ✒️ Notas Adicionales

- **Estado:** Sprint 2 (Hito 2).
- **Conversión automática entre unidades:** fuera del alcance inicial del MVP.
- **Compartir listas mediante WhatsApp:** fuera del alcance inicial del MVP.
- **Posibles complementos:**

## 🔗 Repositorio y gestión del proyecto

- **Repositorio GitHub:** https://github.com/MabelZar/daw-proyecto-cook-list
- **Tablero GitHub Projects:** [Cook-List — Tablero del proyecto](https://github.com/users/MabelZar/projects/1/views/1)
