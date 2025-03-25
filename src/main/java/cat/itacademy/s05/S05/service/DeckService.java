package cat.itacademy.s05.S05.service;

import cat.itacademy.s05.S05.model.enums.Rank;
import cat.itacademy.s05.S05.model.enums.Suit;
import cat.itacademy.s05.S05.exception.domain.DeckIsEmptyException;
import cat.itacademy.s05.S05.model.Card;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DeckService {
    public List<Card> createShuffledDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank value : Rank.values()) {
                deck.add(new Card(value, suit));
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    public Card drawCard(List<Card> deck) {
        if (deck.isEmpty()) {
            throw new DeckIsEmptyException("Deck is empty, cannot draw a card.");
        }
        return deck.remove(0);
    }
}
