package cat.itacademy.s05.t01.n01.service.impl;

import cat.itacademy.s05.t01.n01.exception.InvalidActionException;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.enums.Rank;
import cat.itacademy.s05.t01.n01.model.enums.Suit;
import cat.itacademy.s05.t01.n01.service.CardService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CardServiceImpl implements CardService {

    private static final int DECK_SIZE = 52;

    /**
     * 📌 Genera una lista de cartas aleatorias desde un mazo de 52 cartas.
     * @param quantity Número de cartas a extraer.
     * @return Lista de cartas aleatorias.
     */
    @Override
    public List<Card> drawCards(int quantity) {

        if (quantity <= 0 || quantity > DECK_SIZE) {
            throw new InvalidActionException("La cantidad de cartas solicitadas no es válida: " + quantity);
        }

        List<Card> deck = generateDeck();
        Collections.shuffle(deck);
        return deck.subList(0, quantity);
    }

    /**
     * 📌 Genera un mazo completo de 52 cartas.
     * @return Lista de cartas sin mezclar.
     */
    private List<Card> generateDeck() {
        return Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                .collect(Collectors.toList());
    }
}