package cat.itacademy.s05.S05.exception;

import cat.itacademy.s05.S05.exception.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String error, String message, boolean warn) {
        if (warn) logger.warn("{}: {}", error, message);
        else logger.error("{}: {}", error, message);
        return ResponseEntity.status(status).body(new ErrorResponse(status, error, message));
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGameNotFound(GameNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Game Not Found", ex.getMessage(), false);
    }

    @ExceptionHandler(GameAlreadyEndedException.class)
    public ResponseEntity<ErrorResponse> handleGameAlreadyEnded(GameAlreadyEndedException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Game Already Ended", ex.getMessage(), false);
    }

    @ExceptionHandler(GameNotInProgressException.class)
    public ResponseEntity<ErrorResponse> handleGameNotInProgress(GameNotInProgressException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Game Not In Progress", ex.getMessage(), false);
    }

    @ExceptionHandler(InvalidMoveException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMove(InvalidMoveException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Move", ex.getMessage(), false);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlayerNotFound(PlayerNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Player Not Found", ex.getMessage(), false);
    }

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePlayerAlreadyExists(PlayerAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Player Already Exists", ex.getMessage(), true);
    }

    @ExceptionHandler(DeckIsEmptyException.class)
    public ResponseEntity<ErrorResponse> handleDeckIsEmpty(DeckIsEmptyException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Deck Is Empty", ex.getMessage(), false);
    }

    @ExceptionHandler(RankingIsEmptyException.class)
    public ResponseEntity<ErrorResponse> handleRankingIsEmpty(RankingIsEmptyException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Ranking Is Empty", ex.getMessage(), false);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", errors.toString(), true);
    }
}