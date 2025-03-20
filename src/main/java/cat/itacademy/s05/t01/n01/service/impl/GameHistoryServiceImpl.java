package cat.itacademy.s05.t01.n01.service.impl;

import cat.itacademy.s05.t01.n01.exception.GameHistoryNotFoundException;
import cat.itacademy.s05.t01.n01.exception.InvalidActionException;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.GameHistory;
import cat.itacademy.s05.t01.n01.repository.GameHistoryRepository;
import cat.itacademy.s05.t01.n01.service.GameHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

/**
 * 📌 Implementación del servicio para gestionar el historial de partidas en MongoDB.
 */
@Service
public class GameHistoryServiceImpl implements GameHistoryService {

    private final GameHistoryRepository gameHistoryRepository;

    @Autowired
    public GameHistoryServiceImpl(GameHistoryRepository gameHistoryRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
    }

    /**
     * 📌 Guarda una nueva partida en el historial, validando datos.
     */
    @Override
    public Mono<GameHistory> saveGameHistory(GameHistory gameHistory) {
        if (gameHistory.getGameId() == null || gameHistory.getPlayerId() == null) {
            return Mono.error(new InvalidActionException("El ID del juego y el ID del jugador son obligatorios."));
        }

        if (gameHistory.getPlayerCards() == null || gameHistory.getDealerCards() == null) {
            return Mono.error(new InvalidActionException("Las cartas del jugador y del dealer son obligatorias."));
        }

        return gameHistoryRepository.save(gameHistory);
    }

    /**
     * 📌 Convierte las cartas a `CardRecord` para MongoDB.
     */
    @Override
    public List<GameHistory.CardRecord> convertToCardRecord(List<Card> cards) {
        return cards.stream()
                .map(card -> new GameHistory.CardRecord(card.getSuit(), card.getRank()))
                .toList();
    }

    /**
     * 📌 Obtiene el historial de partidas de un jugador. Si no hay historial, devuelve un error.
     */
    @Override
    public Flux<GameHistory> getPlayerHistory(String playerId) {
        if (playerId == null) {
            return Flux.error(new InvalidActionException("El ID del jugador es obligatorio."));
        }

        return gameHistoryRepository.findByPlayerId(playerId)
                .switchIfEmpty(Flux.error(new GameHistoryNotFoundException("No hay partidas registradas para este jugador.")));
    }
}