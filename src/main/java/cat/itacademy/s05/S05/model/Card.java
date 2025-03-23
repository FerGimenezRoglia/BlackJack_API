package cat.itacademy.s05.S05.model;

import cat.itacademy.s05.S05.model.enums.Rank;
import cat.itacademy.s05.S05.model.enums.Suit;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Card {
    private Rank rank;
    private Suit suit;

    @Override
    public String toString() {
        return rank.toString() + " of " + suit.toString();
    }
}
