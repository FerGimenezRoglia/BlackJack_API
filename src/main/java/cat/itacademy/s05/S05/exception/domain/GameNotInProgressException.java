package cat.itacademy.s05.S05.exception.domain;

public class GameNotInProgressException extends RuntimeException {
    public GameNotInProgressException(String message) {
        super(message);
    }
}
