package cat.itacademy.s05.S05.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Optional;

public enum PlayerMove {
    HIT("Hit"),
    STAND("Stand");

    private final String displayName;

    PlayerMove(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    @Override
    public String toString() {
        return displayName;
    }

    public static Optional<PlayerMove> fromString(String value) {
        return Arrays.stream(PlayerMove.values())
                .filter(move -> move.displayName.equalsIgnoreCase(value) || move.name().equalsIgnoreCase(value))
                .findFirst();
    }
}