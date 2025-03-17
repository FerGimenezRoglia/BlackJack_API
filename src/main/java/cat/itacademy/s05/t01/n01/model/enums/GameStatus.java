package cat.itacademy.s05.t01.n01.model.enums;

import java.util.Scanner;

/**
 * Enum que representa el estado de una partida de Blackjack.
 * Permite almacenar el estado de la partida con un nombre más legible.
 */
public enum GameStatus {
    IN_PROGRESS("En Progreso"), // 📌 La partida sigue en juego
    FINISHED("Finalizada");     // 📌 La partida ha terminado

    private final String displayName; // 📌 Nombre para mostrar en API, logs, etc.

    /**
     * Constructor privado que asigna un nombre personalizado a cada estado.
     * @param displayName Representación legible del estado.
     */
    GameStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Sobrescribe el método `toString()` para devolver el nombre personalizado.
     * En lugar de devolver "IN_PROGRESS", devolverá "En Progreso".
     * @return Nombre del estado en formato legible.
     */
    @Override
    public String toString() {
        return displayName;
    }
}
