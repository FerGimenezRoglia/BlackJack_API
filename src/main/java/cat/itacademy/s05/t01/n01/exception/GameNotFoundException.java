package cat.itacademy.s05.t01.n01.exception;

/**
 * Excepción lanzada cuando no se encuentra una partida en la base de datos.
 * @author Fer_Develop
 */
public class GameNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     * @param message Mensaje de error.
     */
    public GameNotFoundException(String message) {
        super(message);
    }
}