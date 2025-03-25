package cat.itacademy.s05.S05.model.dto;

import cat.itacademy.s05.S05.model.Game;
import cat.itacademy.s05.S05.model.enums.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResponse {
    private String id;
    private GameState state;
    private String playerName;
    private List<String> playerCards;
    private int playerScore;
    private List<String> dealerCards;
    private int dealerScore;
    private String winner;

    public GameResponse(Game game) {
        this.id = game.getId();
        this.state = game.getState();
        this.playerName = game.getPlayerName();
        this.playerCards = game.getPlayerHandCards().getCardNames();
        this.playerScore = game.getPlayerHandCards().getScore();
        this.dealerCards = game.getDealerHandCards().getCardNames();
        this.dealerScore = game.getDealerHandCards().getScore();
        this.winner = game.getWinner();
    }
}
