package cat.itacademy.s05.t01.n01.service.impl;


import cat.itacademy.s05.t01.n01.exception.GameNotFoundException;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.PlayerGame;
import cat.itacademy.s05.t01.n01.model.enums.GameStatus;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerGameRepository;
import cat.itacademy.s05.t01.n01.service.GameService;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final PlayerGameRepository playerGameRepository;

    public GameServiceImpl(GameRepository gameRepository, PlayerService playerService, PlayerGameRepository playerGameRepository) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.playerGameRepository = playerGameRepository;
    }

    @Autowired


    @Override
    public Mono<Game> createGame(String playerName) {
        return playerService.getOrCreatePlayer(playerName)
                .flatMap(player -> {
                    Game newGame = Game.builder()
                            .id(UUID.randomUUID())
                            .status(GameStatus.IN_PROGRESS)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return gameRepository.save(newGame)
                            .flatMap(savedGame -> linkPlayerToGame(player.getId(), savedGame.getId())
                                    .thenReturn(savedGame));
                });

    }

    @Override
    public Mono<Game> getGameById(UUID gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("No se encontró la partida con ID: " + gameId)));
    }

    @Override
    public Mono<Game> playGame(UUID gameId, String action) {
        return null;
    }

    @Override
    public Mono<Void> deleteGame(UUID gameId) {
        return null;
    }

    @Override
    public Flux<Game> getGamesByStatus(GameStatus status) {
        return null;
    }

    private Mono<Void> linkPlayerToGame(UUID playerId, UUID gameId) {
        PlayerGame playerGame = new PlayerGame(playerId, gameId);
        return playerGameRepository.save(playerGame).then();
    }


}
