package cat.itacademy.s05.t01.n01.repository;

import cat.itacademy.s05.t01.n01.model.Card;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import java.util.UUID;

/**
 * 📌 Repositorio reactivo para gestionar las cartas jugadas en las partidas de Blackjack.
 */
public interface CardRepository extends ReactiveCrudRepository<Card, UUID> {

    /**
     * 📌 Obtiene todas las cartas asociadas a una partida específica.
     * @param gameId ID de la partida.
     * @return Lista reactiva (Flux) de cartas en la partida.
     */
    Flux<Card> findByGameId(UUID gameId);
}