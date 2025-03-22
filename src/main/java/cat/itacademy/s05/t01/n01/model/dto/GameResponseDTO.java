package cat.itacademy.s05.t01.n01.model.dto;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.enums.GameStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para devolver datos del game en respuestas.
 * Incluye el ID para evitar problemas con UUID.
 * @author Fer_Develop
 */
@Getter
public class GameResponseDTO {
    private final String id;
    private final String status;
    private final List<CardResponseDTO> playerCards;
    private final List<CardResponseDTO> dealerCards;
    private final String winner;
    private final LocalDateTime createdAt;

    public GameResponseDTO(Game game) {
        this.id = game.getId() != null ? game.getId().toString() : null;
        this.status = game.getStatus().toString();
        this.playerCards = game.getPlayerCards().stream().map(CardResponseDTO::new).collect(Collectors.toList());
        this.dealerCards = game.getDealerCards().stream().map(CardResponseDTO::new).collect(Collectors.toList());
        this.winner = game.getWinner();
        this.createdAt = game.getCreatedAt();
    }
}