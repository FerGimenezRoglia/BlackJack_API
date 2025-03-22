package cat.itacademy.s05.t01.n01.model;

import cat.itacademy.s05.t01.n01.model.enums.Rank;
import cat.itacademy.s05.t01.n01.model.enums.Suit;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 📌 Entidad `GameHistory` para almacenar el historial de partidas en MongoDB.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "game_history") // 📌 Nombre de la colección en MongoDB
public class GameHistory {

    @Id  // 📌 Identificador único generado automáticamente
    private String id;

    private String gameId; // 📌 ID de la partida en MySQL para vinculación

    private String playerId; // 📌 ID del jugador

    private List<CardRecord> playerCards; // 📌 Cartas del jugador

    private List<CardRecord> dealerCards; // 📌 Cartas del dealer

    private String gameResult;

    private String winnerId; // 📌 ID del ganador (puede ser el playerId o "DEALER")

    private String winnerType; // 📌 "PLAYER" o "DEALER"

    private LocalDateTime timestamp; // 📌 Fecha y hora de la partida almacenada

    /**
     * 📌 Subdocumento `CardRecord` para representar las cartas jugadas en el historial.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardRecord {
        private Suit suit; // 📌 Palo de la carta (HEARTS, DIAMONDS, CLUBS, SPADES)
        private Rank rank; // 📌 Valor de la carta (ACE, TWO, ..., KING)
    }
}