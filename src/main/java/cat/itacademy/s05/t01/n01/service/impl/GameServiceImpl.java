package cat.itacademy.s05.t01.n01.service.impl;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.service.GameService;
import reactor.core.publisher.Mono;

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
}
