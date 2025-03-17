package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @author Fer_Develop
 * Interfaz del servicio de jugadores.
 */
public interface PlayerService {

    /**
     * Cambia el nombre de un jugador existente.
     * @param playerId UUID del jugador.
     * @param newName Nuevo nombre del jugador.
     * @return Mono vacío cuando la actualización se complete.
     */
    Mono<Void> updatePlayerName(UUID playerId, String newName);

    /**
     * Obtiene el ranking de jugadores basado en el número de victorias.
     * @return Flux con los jugadores ordenados por ranking.
     */
    Flux<Player> getRanking();
}