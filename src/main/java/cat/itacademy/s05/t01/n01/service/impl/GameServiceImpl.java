package cat.itacademy.s05.t01.n01.service.impl;

import cat.itacademy.s05.t01.n01.exception.GameHistoryNotFoundException;
import cat.itacademy.s05.t01.n01.exception.GameNotFoundException;
import cat.itacademy.s05.t01.n01.exception.InvalidActionException;
import cat.itacademy.s05.t01.n01.model.*;
import cat.itacademy.s05.t01.n01.model.dto.GameHistoryResponseDTO;
import cat.itacademy.s05.t01.n01.model.dto.GameResponseDTO;
import cat.itacademy.s05.t01.n01.model.enums.GameStatus;
import cat.itacademy.s05.t01.n01.model.enums.PlayerAction;
import cat.itacademy.s05.t01.n01.repository.GameHistoryRepository;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerGameRepository;
import cat.itacademy.s05.t01.n01.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final PlayerGameRepository playerGameRepository;
    private final GamePlayService gamePlayService;
    private final CardService cardService;
    private final GameHistoryService gameHistoryService;
    private final GameHistoryRepository gameHistoryRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, PlayerService playerService, PlayerGameRepository playerGameRepository, GamePlayService gamePlayService, CardService cardService, GameHistoryService gameHistoryService, GameHistoryRepository gameHistoryRepository) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.playerGameRepository = playerGameRepository;
        this.gamePlayService = gamePlayService;
        this.cardService = cardService;
        this.gameHistoryService = gameHistoryService;
        this.gameHistoryRepository = gameHistoryRepository;
    }

    @Override
    public Mono<GameResponseDTO> createGame(String playerName) {
        return playerService.getOrCreatePlayer(playerName)
                .flatMap(player -> {
                    Game newGame = Game.builder()
                            .id(UUID.randomUUID())
                            .status(GameStatus.IN_PROGRESS)
                            .playerCards(new ArrayList<>(cardService.drawMultipleCards(2)))
                            .dealerCards(new ArrayList<>(cardService.drawMultipleCards(2)))
                            .createdAt(LocalDateTime.now())
                            .build();
                    return gameRepository.save(newGame)
                            .flatMap(savedGame -> linkPlayerToGame(player.getId(), savedGame.getId())
                                    .then(saveGameHistory(savedGame, player.getId()))
                                    .thenReturn(new GameResponseDTO(savedGame)));
                });
    }

    /**
     * Método para vincular Player con Game en DB.
     */
    private Mono<Void> linkPlayerToGame(UUID playerId, UUID gameId) {
        PlayerGame playerGame = new PlayerGame(playerId, gameId);
        return playerGameRepository.save(playerGame).then();
    }

    /**
     * Método para guardar el estado inicial de la partida en MongoDB.
     */
    private Mono<Void> saveGameHistory(Game game, UUID playerId) {
        GameHistory history = GameHistory.builder()
                .gameId(game.getId().toString())
                .playerId(playerId.toString())
                .playerCards(gameHistoryService.convertToCardRecord(game.getPlayerCards()))
                .dealerCards(gameHistoryService.convertToCardRecord(game.getDealerCards()))
                .gameResult("IN_PROGRESS")
                .timestamp(LocalDateTime.now())
                .build();
        return gameHistoryService.saveGameHistory(history).then();
    }

    @Override
    public Mono<GameResponseDTO> getGameById(UUID gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("No se encontró la partida con ID: " + gameId)))
                .map(game -> new GameResponseDTO(game));
    }

    @Override
    public Mono<GameResponseDTO> playGame(UUID gameId, String action) {
        // 🔹 Validamos la acción ANTES de buscar el juego en la base de datos
        try {
            PlayerAction.valueOf(action.toUpperCase());
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
                })
                .map(GameResponseDTO::new);
    }

    /**
     * Procesa la acción del jugador en una partida en curso.
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
        if (gameId == null) {
            return Mono.error(new InvalidActionException("El ID de la partida no puede ser nulo."));
        }

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("No se encontró la partida con ID: " + gameId)))
                .flatMap(gameRepository::delete)
                .then();
    }

    @Override
    public Flux<GameHistoryResponseDTO> getPlayerHistory(String playerId) {
        if (playerId == null) {
            return Flux.error(new InvalidActionException("El ID del jugador es obligatorio."));
        }
        return gameHistoryRepository.findByPlayerId(playerId)
                .switchIfEmpty(Flux.error(new GameHistoryNotFoundException("No hay partidas registradas para este jugador.")))
                .map(this::convertToResponseDTO);
    }

    /**
     * Método conversor local usado solo en getPlayerHistory.
     */
    private GameHistoryResponseDTO convertToResponseDTO(GameHistory history) {
        return new GameHistoryResponseDTO(
                history.getGameId().toString(),
                history.getPlayerId().toString(),
                history.getPlayerCards(),
                history.getDealerCards(),
                history.getGameResult(),
                history.getTimestamp()
        );
    }

    @Override
    public Flux<GameResponseDTO> getGamesByStatus(GameStatus status) {
        if (status == null) {
            return Flux.error(new InvalidActionException("El estado de la partida no puede ser nulo."));
        }
        return gameRepository.findByStatus(status)
                .map(GameResponseDTO::new);
    }

}
