package cat.itacademy.s05.S05.model;

import cat.itacademy.s05.S05.model.enums.Rank;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Hand {
    private List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        cards.add(card);
    }

    @JsonProperty("score")
    public int getScore() {
        return calculateTotal();
    }

    public int calculateTotal() {
        int total = 0;
        int aceCount = 0;
        for (Card card : cards) {
            total += card.getRank().getValue();
            if (card.getRank() == Rank.ACE) {
                aceCount++;
            }
        }

        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }
        return total;
    }

    public boolean isBust() {
        return calculateTotal() > 21;
    }

    public List<String> getCardNames() {
        return cards.stream()
                .map(Card::toString)
                .collect(Collectors.toList());
    }
}