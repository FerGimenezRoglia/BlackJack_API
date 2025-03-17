package cat.itacademy.s05.t01.n01.model;

import cat.itacademy.s05.t01.n01.model.enums.Suit;
import cat.itacademy.s05.t01.n01.model.enums.Rank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * 📌 Entidad `GameCard` que representa una carta dentro de una partida de Blackjack.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("game_card") // 📌 Mapeo a la tabla `game_card` en MySQL
public class Card {

    @Id // 📌 Identificador único de la carta
    private UUID id;

    private UUID gameId; // 📌 Relación con la partida en la que se jugó esta carta

    private Suit suit; // 📌 El palo de la carta (HEART, DIAMOND, CLUB, SPADE)

    private Rank rank; // 📌 El valor de la carta (TWO, THREE, ..., KING, ACE)

    public Card(Suit suit, Rank rank) {
        this.id = UUID.randomUUID();
        this.suit = suit;
        this.rank = rank;
    }
}