package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameHistory;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.model.enums.GameStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 📌 Servicio para gestionar las partidas del juego.
 */
public interface GameService {

    /**
     * 📌 Crea una nueva partida de Blackjack.
     * @param playerName Nombre del jugador.
     * @return Mono con la partida creada.
     */
    Mono<Game> createGame(String playerName);

    /**
     * 📌 Obtiene los detalles de una partida específica.
     * @param gameId ID de la partida.
     * @return Mono con la partida encontrada.
     */
    Mono<Game> getGameById(UUID gameId);

    Mono<Game> playGame(UUID gameId, String action);

    /**
     * 📌 Elimina una partida específica.
     * @param gameId ID de la partida a eliminar.
     * @return Mono vacío si la operación es exitosa.
     */
    Mono<Void> deleteGame(UUID gameId);

    /**
     * 📌 Obtiene el historial de partidas de un jugador en MongoDB.
     * @param playerId ID del jugador.
     * @return Flux con el historial de partidas.
     */
    Flux<GameHistory> getPlayerHistory(String playerId);

    /**
     * 📌 Obtiene todas las partidas según su estado (IN_PROGRESS, FINISHED).
     * @param status Estado de las partidas.
     * @return Flujo con las partidas encontradas.
     */
    Flux<Game> getGamesByStatus(GameStatus status);
}