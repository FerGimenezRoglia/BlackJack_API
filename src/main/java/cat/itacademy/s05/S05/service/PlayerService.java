package cat.itacademy.s05.S05.service;

import cat.itacademy.s05.S05.model.Game;
import cat.itacademy.s05.S05.model.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {
    Mono<Player> updatePlayerName(Long playerId, String newName);
    Flux<Player> getRanking();
    Mono<Player> findByName(String name);
    Mono<Player> save(Player player);
    Mono<Void> updatePlayerStats(Game game);
}
