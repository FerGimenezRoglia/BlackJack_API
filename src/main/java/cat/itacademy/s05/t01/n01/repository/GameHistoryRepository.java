package cat.itacademy.s05.t01.n01.repository;

import cat.itacademy.s05.t01.n01.model.GameHistory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * 📌 Repositorio Reactivo para el historial de partidas en MongoDB.
 */
public interface GameHistoryRepository extends ReactiveMongoRepository<GameHistory, String> {

    /**
     * 📌 Encuentra todas las partidas de un jugador por su ID.
     * @param playerId ID del jugador como String.
     * @return Flujo de historial de partidas.
     */
    Flux<GameHistory> findByPlayerId(String playerId);

}