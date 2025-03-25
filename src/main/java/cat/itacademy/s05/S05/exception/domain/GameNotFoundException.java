package cat.itacademy.s05.S05.exception.domain;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super(message);
    }
}
