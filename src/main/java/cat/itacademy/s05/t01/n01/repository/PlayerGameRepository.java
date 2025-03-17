package cat.itacademy.s05.t01.n01.repository;

import cat.itacademy.s05.t01.n01.model.PlayerGame;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import java.util.UUID;

/**
 * 📌 Repositorio Reactivo para la relación `player_game` (jugador <-> partida).
 */
public interface PlayerGameRepository extends ReactiveCrudRepository<PlayerGame, UUID> {

    /**
     * 📌 Obtiene todas las partidas de un jugador.
     * @param playerId ID del jugador.
     * @return Flujo reactivo de las partidas en las que participó el jugador.
     */
    Flux<PlayerGame> findByPlayerId(UUID playerId);
}