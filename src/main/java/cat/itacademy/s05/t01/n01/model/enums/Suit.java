package cat.itacademy.s05.t01.n01.model.enums;

/**
 * 📌 Enum que representa los cuatro palos de una baraja estándar.
 */
public enum Suit {
    HEART("Corazón ♥"),
    DIAMOND("Diamante ♦"),
    CLUB("Trébol ♣"),
    SPADE("Pica ♠");

    private final String displayName; // 📌 Nombre amigable para mostrar

    Suit(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}