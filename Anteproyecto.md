
# Anteproyecto: Cook-List

**Módulo:** Proyecto Intermodular (0379 · PIDW2) — 2.º DAW
**Centro:** CIFP Avilés (Curso 2026/2027)  
**Autor/a:** Mabel Zarate Espíritu  
**Repositorio GitHub:** https://github.com/MabelZar/daw-proyecto-cook-list 
**Tablero GitHub Projects:** https://github.com/users/MabelZar/projects/1/views/1

---

## 1. Justificación y dominio funcional

### 1.1. Elevator pitch (100–150 palabras)

Cook-List es una aplicación web destinada a facilitar la planificación semanal de comidas y la organización de la compra doméstica. Su objetivo es ayudar a las personas y familias a organizar sus menús, controlar los ingredientes necesarios y preparar una lista de la compra ajustada al número de comensales. Los usuarios podrán registrarse, gestionar su perfil y los datos de su hogar, crear sus propios platos e indicar sus ingredientes, cantidades, unidades de medida y número de raciones de referencia. A partir de estos datos, podrán diseñar menús semanales y calcular las cantidades de ingredientes necesarias según las personas que vayan a comer. Finalmente, la aplicación generará una lista de la compra basada en la planificación realizada. Cook-List busca reducir el tiempo dedicado a organizar las comidas, mejorar la planificación doméstica y facilitar una compra más ordenada. La conversión automática de unidades y el envío de listas por WhatsApp se plantean como posibles mejoras futuras.

### 1.2. Público objetivo y contexto de uso

Cook-List está dirigida a personas que desean organizar sus comidas y compras semanales de forma sencilla, especialmente familias, personas que comparten vivienda y responsables de la compra doméstica.

La aplicación se utilizará desde un navegador web, permitiendo gestionar recetas o platos, planificar los menús de la semana y generar listas de la compra. El cálculo de las cantidades se adaptará al número de comensales indicado, facilitando la organización de los ingredientes necesarios.

---

## 2. Roles del sistema y casos de uso

### 2.1. Matriz de roles (RBAC)

- **Administrador:** podrá consultar las cuentas de los usuarios registrados, desactivar cuentas cuando sea necesario y consultar métricas generales de uso de la aplicación.
- **Usuario registrado:** podrá gestionar su perfil y los datos de su hogar, crear, consultar, modificar y eliminar sus platos, gestionar sus ingredientes y cantidades, planificar menús semanales y generar listas de la compra.
- **Público / invitado:** podrá consultar la información general de la aplicación y acceder a las opciones de registro e inicio de sesión. No podrá gestionar platos, menús ni listas de la compra sin autenticarse.

### 2.2. Casos de uso principales (mínimo 6)

1. **CU-01: Registro e inicio de sesión.** Permitir que una persona cree una cuenta y acceda a la aplicación de forma segura.
2. **CU-02: Gestión del perfil y del hogar.** Permitir al usuario registrado modificar sus datos y configurar la información necesaria para planificar las comidas, incluido el número de comensales.
3. **CU-03: Gestión de platos.** Permitir crear, consultar, modificar, eliminar y buscar platos personalizados.
4. **CU-04: Gestión de ingredientes.** Permitir asociar ingredientes a los platos, indicando cantidades, unidades de medida y número de raciones de referencia.
5. **CU-05: Planificación del menú semanal.** Permitir organizar los platos en los distintos días de la semana y modificar la planificación.
6. **CU-06: Cálculo de cantidades.** Calcular las cantidades de ingredientes necesarias en función del número de comensales indicado y las raciones de referencia de cada plato.
7. **CU-07: Generación de la lista de la compra.** Generar y gestionar una lista de la compra a partir de los ingredientes necesarios para el menú planificado.
8. **CU-08: Administración de cuentas.** Permitir al administrador consultar las cuentas registradas y desactivar usuarios.
9. **CU-09: Consulta de métricas generales.** Permitir al administrador consultar indicadores generales de uso de la aplicación.

### 2.3. Alcance del MVP (Hito 3, diciembre)

- **Incluido:**
  - Registro e inicio de sesión de usuarios.
  - Gestión básica del perfil y de los datos del hogar.
  - Operaciones CRUD de platos e ingredientes.
  - Planificación y modificación del menú semanal.
  - Cálculo de cantidades de ingredientes según el número de comensales.
  - Generación y gestión de listas de la compra.
  - Interfaz web desarrollada con React.
  - API REST desarrollada con Java y Spring Boot.
  - Persistencia de datos en PostgreSQL.
  - Pruebas de los endpoints de la API mediante Postman.

- **Fuera del MVP:**
  - Conversión automática entre diferentes unidades de medida.
  - Compartir directamente las listas de la compra mediante WhatsApp.

---

## 3. Modelo de datos

- **Diagrama Entidad-Relación en 3FN:** 
   [Ver diagrama Entidad-Relación](docs/der.md)
   
- **Relación 1:N:**
  - `ROLES → USERS`: un rol puede estar asignado a varios usuarios y cada usuario tiene un único rol.
  - `USERS → RECIPES`: un usuario puede crear varias recetas y cada receta pertenece a un único usuario.
  - `CATEGORY → RECIPES`: una categoría puede clasificar varias recetas y cada receta pertenece a una categoría.

- **Relación M:N con atributos:** 
  - `RECIPES ↔ INGREDIENTS` mediante la tabla intermedia `RECIPE_INGREDIENTS(recipe_id, ingredient_id, quantity)`: una receta puede contener varios ingredientes y un ingrediente puede utilizarse en varias recetas. La tabla intermedia almacena la cantidad utilizada de cada ingrediente.

---

## 4. Stack tecnológico previsto

- **Motor de base de datos:** PostgreSQL. Se utilizará para almacenar la información de usuarios, platos, ingredientes, menús y listas de la compra, manteniendo las relaciones entre las entidades.
- **Backend API:** Java con Spring Boot. Se empleará para implementar la API REST, las validaciones y la lógica de negocio. La aplicación se organizará en capas de controladores, servicios y acceso a datos.
- **Persistencia backend:** Spring Data JPA e Hibernate para mapear las entidades Java a las tablas de PostgreSQL y facilitar las operaciones de persistencia.
- **Frontend:** React.js con HTML, CSS y JavaScript. React permitirá construir una interfaz dinámica mediante componentes reutilizables; CSS se utilizará para definir la presentación visual.
- **Construcción del backend:** Maven para gestionar dependencias y automatizar las tareas de compilación.
- **Pruebas de API:** Postman para probar los endpoints y comprobar las respuestas de la API REST.
- **Entorno de desarrollo:** Visual Studio Code para el trabajo con el frontend y los archivos del proyecto.
- **Alojamiento cloud previsto:**  Railway por su facilidad de uso, su integración con GitHub y la posibilidad de alojar el backend de Spring Boot y la base de datos PostgreSQL en una misma plataforma

---

## 5. Selección tentativa de Add-ons (mínimo 2)

- [x] Dockerización multicontenedor con Docker Compose (+1.0)
- [ ] Pipeline CI/CD automatizado con GitHub Actions (+1.0)
- [ ] Almacenamiento desacoplado en AWS S3 / Cloudinary (+0.5)
- [ ] Pasarela de pagos Stripe en entorno Sandbox con Webhooks (+1.0)
- [ ] Generación de informes en PDF/Excel en backend (+0.5)
- [ ] WebSockets / Comunicación en tiempo real (+1.0)
- [ ] Batería de pruebas automatizadas (>40 % de endpoints) (+1.0)
- [x] Documentación OpenAPI / Swagger interactiva (+0.5)

