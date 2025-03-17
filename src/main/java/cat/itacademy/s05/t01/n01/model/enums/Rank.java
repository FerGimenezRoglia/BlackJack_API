package cat.itacademy.s05.t01.n01.model.enums;

/**
 * Enum que representa los valores de las cartas en Blackjack.
 * Cada carta tiene un valor asociado para facilitar los cálculos del juego.
 */
public enum Rank {
    ACE(11),      // 📌 Los Ases pueden valer 11 (pero luego podemos cambiarlo a 1 si es necesario)
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),    // 📌 Cartas con figuras valen 10 puntos en Blackjack
    QUEEN(10),
    KING(10);

    private final int value; // 📌 Almacena el valor numérico de la carta

    /**
     * Constructor privado que asigna el valor numérico a cada carta.
     * @param value Valor de la carta en Blackjack.
     */
    Rank(int value) {
        this.value = value;
    }

    /**
     * Devuelve el valor numérico de la carta.
     * @return Valor de la carta (ej: 10 para JACK, 11 para ACE).
     */
    public int getValue() {
        return value;
    }

    /**
     * Sobrescribe `toString()` para mostrar el nombre con la primera letra en mayúscula.
     * Ejemplo: "ACE" → "Ace", "QUEEN" → "Queen"
     * @return Nombre de la carta en formato más legible.
     */
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}