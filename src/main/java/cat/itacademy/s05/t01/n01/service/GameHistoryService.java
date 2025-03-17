package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.GameHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 📌 Interfaz del servicio para gestionar el historial de partidas en MongoDB.
 */
public interface GameHistoryService {

    /**
     * 📌 Guarda una nueva partida en el historial.
     * @param gameHistory Objeto con la información de la partida.
     * @return Mono con la partida guardada.
     */
    Mono<GameHistory> saveGameHistory(GameHistory gameHistory);

    /**
     * 📌 Obtiene el historial de partidas de un jugador por su ID.
     * @param playerId ID del jugador.
     * @return Flux con las partidas jugadas por el jugador.
     */
    Flux<GameHistory> getPlayerHistory(UUID playerId);
}