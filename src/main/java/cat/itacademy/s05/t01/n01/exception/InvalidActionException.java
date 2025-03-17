package cat.itacademy.s05.t01.n01.exception;

/**
 * Excepción personalizada para manejar acciones no válidas dentro del juego.
 * Se lanza cuando un jugador intenta realizar una acción inválida según las reglas del Blackjack.
 *
 * @author Fer_Develop
 */
public class InvalidActionException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje personalizado.
     * @param message Mensaje de error que describe la causa de la excepción.
     */
    public InvalidActionException(String message) {
        super(message);
    }
}