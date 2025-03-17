package cat.itacademy.s05.t01.n01.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para la API.
 * Captura y responde adecuadamente a las excepciones personalizadas y generales.
 *
 * @author Fer_Develop
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja la excepción cuando una partida no es encontrada.
     * @param ex Excepción específica de juego no encontrado.
     * @return ResponseEntity con mensaje de error y estado 404.
     */
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<String> handleGameNotFoundException(GameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Maneja la excepción cuando un jugador no es encontrado.
     * @param ex Excepción específica de jugador no encontrado.
     * @return ResponseEntity con mensaje de error y estado 404.
     */
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> handlePlayerNotFoundException(PlayerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * 📌 Maneja intentos de buscar un historial de juego que no existe.
     */
    @ExceptionHandler(GameHistoryNotFoundException.class)
    public ResponseEntity<String> handleGameHistoryNotFound(GameHistoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Maneja intentos de acciones inválidas en el juego.
     * @param ex Excepción de acción inválida.
     * @return ResponseEntity con mensaje de error y estado 400.
     */
    @ExceptionHandler(InvalidActionException.class)
    public ResponseEntity<String> handleInvalidActionException(InvalidActionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Manejo genérico de excepciones no controladas.
     * @param ex Excepción desconocida.
     * @return ResponseEntity con mensaje de error y estado 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error inesperado: " + ex.getMessage());
    }
}