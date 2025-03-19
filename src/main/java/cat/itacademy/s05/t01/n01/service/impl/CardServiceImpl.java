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
    private static final List<Card> DECK = Stream.of(Suit.values())
            .flatMap(suit -> Stream.of(Rank.values())
                    .map(rank -> new Card(suit, rank)))
            .collect(Collectors.toList());

    /**
     * 📌 Extrae una única carta aleatoria del mazo.
     *
     * @return Una carta aleatoria.
     */
    @Override
    public Card drawSingleCard() {
        List<Card> shuffledeck = new ArrayList<>(DECK);
        Collections.shuffle(shuffledeck);
        return shuffledeck.get(0);
    }

    /**
     * 📌 Genera una lista de cartas aleatorias desde un mazo de 52 cartas.
     *
     * @param quantity Número de cartas a extraer.
     * @return Lista de cartas aleatorias.
     */
    @Override
    public List<Card> drawMultipleCards(int quantity) {
        if (quantity <= 0 || quantity > DECK_SIZE) {
            throw new InvalidActionException("La cantidad de cartas solicitadas no es válida: " + quantity);
        }
        List<Card> shuffledDeck = new ArrayList<>(DECK); // Copiamos el mazo original
        Collections.shuffle(shuffledDeck); // Mezclamos
        return shuffledDeck.subList(0, Math.min(quantity, DECK_SIZE)); // Sacamos `quantity` cartas
    }
}