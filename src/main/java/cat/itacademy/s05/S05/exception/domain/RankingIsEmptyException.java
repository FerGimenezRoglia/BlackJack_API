package cat.itacademy.s05.S05.exception.domain;

public class RankingIsEmptyException extends RuntimeException {
    public RankingIsEmptyException(String message) {
        super(message);
    }
}