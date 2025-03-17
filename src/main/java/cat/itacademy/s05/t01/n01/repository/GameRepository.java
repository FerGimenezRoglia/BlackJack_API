package cat.itacademy.s05.t01.n01.repository;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.enums.GameStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GameRepository extends ReactiveCrudRepository<Game, UUID> {

    Mono<Game> findById(UUID id);

    Flux<Game> findByStatus(GameStatus status);
}
