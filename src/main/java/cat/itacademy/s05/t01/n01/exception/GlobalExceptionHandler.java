package cat.itacademy.s05.t01.n01.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *  **GlobalExceptionHandler - Manejador centralizado de excepciones**
 *
 * 📌 Captura y maneja excepciones específicas y generales en la API.
 * 📌 Responde con códigos de estado HTTP adecuados según el error detectado.
 * 📌 Mejora la organización del código evitando `try-catch` repetitivos en los controladores.
 *
 * @author Fer_Develop
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *  **Excepción: Partida no encontrada**
     *
     * 📌 Se lanza cuando se intenta acceder a una partida inexistente.
     * @param ex Excepción específica de juego no encontrado.
     * @return `404 Not Found` con un mensaje descriptivo.
     */
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<String> handleGameNotFoundException(GameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     *  **Excepción: Jugador no encontrado**
     *
     * 📌 Se activa cuando no se encuentra un jugador en la base de datos.
     * @param ex Excepción específica de jugador no encontrado.
     * @return `404 Not Found` con un mensaje explicativo.
     */
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> handlePlayerNotFoundException(PlayerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     *  **Excepción: Historial de partidas no encontrado**
     *
     * 📌 Ocurre cuando se intenta consultar el historial de un jugador sin registros previos.
     * @param ex Excepción de historial de juego no encontrado.
     * @return `404 Not Found` con un mensaje informativo.
     */
    @ExceptionHandler(GameHistoryNotFoundException.class)
    public ResponseEntity<String> handleGameHistoryNotFound(GameHistoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * ⚠ **Excepción: Jugador duplicado**
     *
     * 📌 Se lanza cuando se intenta registrar un jugador con un nombre ya existente.
     * @param ex Excepción específica de duplicación de jugador.
     * @return `409 Conflict` con un mensaje de error.
     */
    @ExceptionHandler(DuplicatePlayerException.class)
    public ResponseEntity<String> handleDuplicatePlayerException(DuplicatePlayerException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     *  **Excepción: Acción inválida en el juego**
     *
     * 📌 Se activa cuando el usuario intenta realizar una acción no permitida (ej: acción desconocida en Blackjack).
     * @param ex Excepción de acción inválida.
     * @return `400 Bad Request` con detalles del error.
     */
    @ExceptionHandler(InvalidActionException.class)
    public ResponseEntity<String> handleInvalidActionException(InvalidActionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     *  **Excepción: UUID con formato incorrecto**
     *
     * 📌 Se lanza cuando un identificador UUID no tiene el formato adecuado.
     * @param ex Excepción generada por un argumento inválido.
     * @return `400 Bad Request` con un mensaje de error detallado.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleInvalidUUIDException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El formato del UUID proporcionado no es válido: " + ex.getMessage());
    }

    /**
     * ⚠ **Excepción: Error inesperado**
     *
     * 📌 Captura cualquier error no controlado dentro de la API.
     * 📌 Se usa como un "catch-all" para evitar exponer detalles sensibles al usuario.
     * @param ex Excepción genérica no manejada previamente.
     * @return `500 Internal Server Error` con un mensaje de error genérico.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error inesperado: " + ex.getMessage());
    }
}