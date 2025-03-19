package cat.itacademy.s05.t01.n01.service.impl;

import cat.itacademy.s05.t01.n01.exception.GameNotFoundException;
import cat.itacademy.s05.t01.n01.exception.InvalidActionException;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameHistory;
import cat.itacademy.s05.t01.n01.model.PlayerGame;
import cat.itacademy.s05.t01.n01.model.enums.GameStatus;
import cat.itacademy.s05.t01.n01.model.enums.PlayerAction;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerGameRepository;
import cat.itacademy.s05.t01.n01.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final PlayerGameRepository playerGameRepository;
    private final GamePlayService gamePlayService;
    private final CardService cardService;
    private final GameHistoryService gameHistoryService;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, PlayerService playerService, PlayerGameRepository playerGameRepository, GamePlayService gamePlayService, CardService cardService, GameHistoryService gameHistoryService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.playerGameRepository = playerGameRepository;
        this.gamePlayService = gamePlayService;
        this.cardService = cardService;
        this.gameHistoryService = gameHistoryService;
    }

    @Override
    public Mono<Game> createGame(String playerName) {
        return playerService.getOrCreatePlayer(playerName)
                .flatMap(player -> {
                    Game newGame = Game.builder()
                            .id(UUID.randomUUID())
                            .status(GameStatus.IN_PROGRESS)
                            .playerCards(new ArrayList<>(cardService.drawMultipleCards(2))) // 🔹 2 cartas para el jugador
                            .dealerCards(new ArrayList<>(cardService.drawMultipleCards(2))) // 🔹 2 cartas para el dealer
                            .createdAt(LocalDateTime.now())
                            .build();
                    return gameRepository.save(newGame)
                            .flatMap(savedGame -> linkPlayerToGame(player.getId(), savedGame.getId())
                                    .then(saveGameHistory(savedGame, player.getId()))
                                    .thenReturn(savedGame));
                });
    }

    private Mono<Void> linkPlayerToGame(UUID playerId, UUID gameId) {
        PlayerGame playerGame = new PlayerGame(playerId, gameId);
        return playerGameRepository.save(playerGame).then();
    }

    /**
     * Método para guardar el estado inicial de la partida en MongoDB.
     */
    private Mono<Void> saveGameHistory(Game game, UUID playerId) {
        GameHistory history = GameHistory.builder()
                .gameId(game.getId())
                .playerId(playerId)
                .playerCards(gameHistoryService.convertToCardRecord(game.getPlayerCards()))
                .dealerCards(gameHistoryService.convertToCardRecord(game.getDealerCards()))
                .gameResult("IN_PROGRESS")
                .timestamp(LocalDateTime.now())
                .build();
        return gameHistoryService.saveGameHistory(history).then();
    }

    @Override
    public Mono<Game> getGameById(UUID gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("No se encontró la partida con ID: " + gameId)));
    }

    @Override
    public Mono<Game> playGame(UUID gameId, String action) {
        // 🔹 Validamos la acción ANTES de buscar el juego en la base de datos
        try {
            PlayerAction playerAction = PlayerAction.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Mono.error(new InvalidActionException("Acción inválida: " + action));
        }

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("No se encontró la partida con ID: " + gameId)))
                .flatMap(game -> {
                    if (game.getStatus() == GameStatus.FINISHED) {
                        return Mono.error(new InvalidActionException("La partida ya está finalizada y no se puede jugar."));
                    }
                    return processPlayerAction(Mono.just(game), PlayerAction.valueOf(action.toUpperCase()));
                });
    }

    /**
     * 📌 Procesa la acción del jugador en una partida en curso.
     * @param gameMono La partida en la que se juega.
     * @param action La acción elegida por el jugador (HIT o STAND).
     * @return La partida actualizada después de la acción.
     */
    private Mono<Game> processPlayerAction(Mono<Game> gameMono, PlayerAction action) {
        return gameMono.flatMap(game -> {
            switch (action) {
                case HIT:
                    return gamePlayService.handleHit(game);
                case STAND:
                    return gamePlayService.handleStand(game);
                default:
                    return Mono.error(new InvalidActionException("Acción inválida: " + action));
            }
        });
    }

    @Override
    public Mono<Void> deleteGame(UUID gameId) {
        return null;
    }

    @Override
    public Flux<Game> getGamesByStatus(GameStatus status) {
        return null;
    }




}
