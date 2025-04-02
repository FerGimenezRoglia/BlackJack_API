# 🃏 Reactive Blackjack API con Spring WebFlux

## 📋 Descripción

Aplicación backend desarrollada en Java utilizando Spring Boot y el enfoque **reactivo con WebFlux**. Implementa las reglas de un juego de **Blackjack** con funcionalidades completas: gestión de jugadores, partidas, movimientos y ranking.

La aplicación conecta y gestiona datos en **dos bases de datos distintas**:  
- 🟢 **MongoDB** para datos del juego en tiempo real  
- 🔵 **MySQL** para información persistente de jugadores y estadísticas

Incluye pruebas unitarias, documentación Swagger y soporte para despliegue con Docker.

## 🧪 Requisitos Técnicos

- Java 17 o superior  
- Maven  
- Spring Boot  
- MongoDB  
- MySQL  
- Docker  
- Postman (u otra herramienta de testeo de APIs)  
- IDE (IntelliJ IDEA recomendado)

## ⚙️ Dependencias Utilizadas

- `spring-boot-starter-webflux`  
- `spring-boot-starter-data-mongodb-reactive`  
- `spring-boot-starter-data-r2dbc`  
- `mysql-connector-java`  
- `reactor-test`, `junit`, `mockito`  
- `springdoc-openapi` (Swagger UI)  
- `lombok`

## 📁 Estructura del Proyecto

| Módulo         | Descripción                                 |
|----------------|---------------------------------------------|
| `controller`   | Controladores REST reactivos                |
| `model`        | Entidades del juego y DTOs                  |
| `repository`   | Repositorios reactivos (MongoDB y MySQL)    |
| `service`      | Lógica de negocio                           |
| `exception`    | Manejo global de excepciones                |
| `dto`          | Objetos de transferencia (request/response) |

## 🧩 Configuración de Bases de Datos

- **MongoDB**: almacena estados de las partidas en tiempo real  
- **MySQL**: guarda información persistente sobre jugadores y estadísticas

## 🔗 Endpoints Principales

### 🎮 Crear nueva partida  
**POST** `/game/new`  
Body:
```json
{ "playerName": "JugadorX" }
```

### 📄 Obtener detalles de partida  
**GET** `/game/{id}`

### 🃏 Realizar jugada  
**POST** `/game/{id}/play`  
Body:
```json
{ "move": "Hit" }
```

### ❌ Eliminar partida  
**DELETE** `/game/{id}/delete`

### 🏆 Obtener ranking  
**GET** `/ranking`

### ✏️ Cambiar nombre del jugador  
**PUT** `/player/{playerId}`  
Body:
```json
{ "playerName": "NuevoNombre" }
```

## 🚨 Manejo de Errores

- `@GlobalExceptionHandler` centralizado  
- Errores de validación (`@Valid`) tratados con `WebExchangeBindException`  
- Mensajes personalizados para:
  - Partida o jugador no encontrado → 404  
  - Movimiento inválido → 400  
  - Error del servidor → 500

## ✅ Testing

- Pruebas unitarias con **JUnit + Mockito + StepVerifier**
- Cubren servicios y controladores
- Prueban lógica del juego y flujos reactivos

## 🧪 Documentación Swagger

Disponible en:  
📍 `http://localhost:8080/swagger-ui.html`

## 🚀 Instrucciones para ejecutar

```bash
# 1. Clona el repositorio
git clone <URL_DEL_REPO>

# 2. Construí el proyecto
mvn clean install

# 3. Levantá la app con Docker
docker-compose up --build
```

La API estará disponible en:  
📍 `http://localhost:8080`