package cat.itacademy.s05.t01.n01.exception;

public class GameHistoryNotFoundException extends RuntimeException {
    public GameHistoryNotFoundException(String message) {
        super(message);
    }
}
