package cat.itacademy.s05.t01.n01.exception;

/**
 * Excepción personalizada para manejar casos en los que un jugador no es encontrado.
 * Se lanza cuando se intenta acceder a un jugador inexistente en la base de datos.
 *
 * @author Fer_Develop
 */
public class PlayerNotFoundException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje personalizado.
     * @param message Mensaje de error que describe la causa de la excepción.
     */
    public PlayerNotFoundException(String message) {
        super(message);
    }
}