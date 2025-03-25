package cat.itacademy.s05.S05.service;

import cat.itacademy.s05.S05.model.Game;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {
    Mono<Game> createGame(String playerName);
    Flux<Game> getAllGames();
    Mono<Game> getGame(String gameId);
    Mono<Game> playMove(String gameId, String move);
    Mono<Void> deleteGame(String gameId);
}
