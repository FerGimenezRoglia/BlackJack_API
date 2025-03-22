package cat.itacademy.s05.t01.n01.model.dto;

import cat.itacademy.s05.t01.n01.model.GameHistory.CardRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GameHistoryResponseDTO {
    private String gameId;
    private String playerId;
    private List<CardRecord> playerCards;
    private List<CardRecord> dealerCards;
    private String gameResult;
    private LocalDateTime timestamp;
}