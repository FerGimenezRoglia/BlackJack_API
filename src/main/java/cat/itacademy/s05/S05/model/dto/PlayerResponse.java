package cat.itacademy.s05.S05.model.dto;

import cat.itacademy.s05.S05.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponse {
    private Long id;
    private String name;
    private int wins;

    public PlayerResponse(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.wins = player.getWins();
    }
}
