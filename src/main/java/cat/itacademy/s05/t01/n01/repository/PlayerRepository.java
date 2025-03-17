package cat.itacademy.s05.t01.n01.repository;

import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface PlayerRepository extends R2dbcRepository<Player, UUID> {
    Flux<Player> findTop10ByOrderByTotalWinsDesc();
}
