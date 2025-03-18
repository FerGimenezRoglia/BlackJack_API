package cat.itacademy.s05.t01.n01.service.impl;

import cat.itacademy.s05.t01.n01.exception.InvalidActionException;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.model.enums.GameStatus;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.service.GameService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameServiceImpl(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }


    @Override
    public Mono<Game> createGame(String playerName) {
        return null;
    }

    @Override
    public Mono<Game> getGameById(UUID gameId) {
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

    private Mono<Player> findOrCreatePlayer(UUID playerId, String playerName){
        return playerRepository.findById(playerId)
                .switchIfEmpty(playerRepository.findById(playerId))
                .switchIfEmpty(Mono.defer(() ->
                        if (playerName == null || playerName.isBlank()){
                            return Mono.error(new InvalidActionException("El nombre del jugador no puede estar vacío."));
                            Player newPlayer = new Player(UUID.randomUUID(), playerName, 0, 0);
        }));

    }
}
