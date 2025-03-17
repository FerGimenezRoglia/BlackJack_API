package cat.itacademy.s05.t01.n01.model;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;
import java.util.UUID;

/**
 * 📌 Representa la relación entre un jugador y una partida en MySQL.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("player_game") // 📌 Nombre de la tabla en MySQL
public class PlayerGame {
    private UUID playerId; // 📌 ID del jugador
    private UUID gameId;   // 📌 ID de la partida
}