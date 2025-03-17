package cat.itacademy.s05.t01.n01.model.dto;

import cat.itacademy.s05.t01.n01.model.Player;
import lombok.Getter;

/**
 * DTO para devolver datos del jugador en el ranking.
 * @author Fer_Develop
 */
@Getter
public class PlayerResponseDTO {
    private final String name;
    private final int totalWins;

    public PlayerResponseDTO(Player player) {
        this.name = player.getName();
        this.totalWins = player.getTotalWins();
    }
}