package cat.itacademy.s05.S05.exception.domain;

public class GameAlreadyEndedException extends RuntimeException {
    public GameAlreadyEndedException(String message) {
        super(message);
    }
}
