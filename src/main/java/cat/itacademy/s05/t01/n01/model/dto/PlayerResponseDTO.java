package cat.itacademy.s05.t01.n01.model.dto;

import cat.itacademy.s05.t01.n01.model.Player;
import lombok.Getter;

/**
 * DTO para devolver datos del jugador en respuestas.
 * Incluye el ID para evitar problemas con UUID.
 * @author Fer_Develop
 */
@Getter
public class PlayerResponseDTO {
    private final String id;  // 🔹 Convertimos UUID a String
    private final String name;
    private final int totalWins;

    public PlayerResponseDTO(Player player) {
        this.id = player.getId().toString();
        this.name = player.getName();
        this.totalWins = player.getTotalWins();
    }
}