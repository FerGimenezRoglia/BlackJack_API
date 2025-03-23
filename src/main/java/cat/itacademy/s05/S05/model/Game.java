package cat.itacademy.s05.S05.model;

import cat.itacademy.s05.S05.model.enums.GameState;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "games")
@JsonPropertyOrder({"id", "playerName", "state", "playerHandCards", "dealerHandCards", "winner", "deckRemainingCards"})
public class Game {
    @Id
    private String id;
    private String playerName;
    private GameState state;
    private Hand playerHandCards;
    private Hand dealerHandCards;
    private String winner;
    private List<Card> deckRemainingCards;
}