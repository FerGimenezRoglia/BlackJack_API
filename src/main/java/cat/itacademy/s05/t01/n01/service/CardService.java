package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Card;
import java.util.List;

public interface CardService {
    /**
     * Método para extraer una cantidad de cartas aleatorias del mazo.
     * @param quantity Cantidad de cartas a extraer.
     * @return Lista de cartas extraídas.
     */
    List<Card> drawCards(int quantity);
}