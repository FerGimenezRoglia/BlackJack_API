package cat.itacademy.s05.S05.exception.domain;

public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(String message) {
        super(message);
    }
}
