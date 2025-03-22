package cat.itacademy.s05.t01.n01.model.dto;

import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.enums.Rank;
import cat.itacademy.s05.t01.n01.model.enums.Suit;
import lombok.Getter;

/**
 * DTO para devolver datos de las cartas en respuestas.
 * Incluye el ID para evitar problemas con UUID.
 * @author Fer_Develop
 */
@Getter
public class CardResponseDTO {
    private final String id;
    private final String gameId;
    private final Suit suit;
    private final Rank rank;

    public CardResponseDTO(Card card) {
        this.id = card.getId() != null ? card.getId().toString() : null;
        this.gameId = card.getGameId() != null ? card.getGameId().toString() : null;
        this.suit = card.getSuit();
        this.rank = card.getRank();
    }
}