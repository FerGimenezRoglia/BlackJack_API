package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Game;
import reactor.core.publisher.Mono;

/**
 * 📌 Servicio para manejar la lógica del juego (HIT y STAND).
 * @author Fer_Develop
 */
public interface GamePlayService {
    Mono<Game> handleHit(Game game);
    Mono<Game>handleStand(Game game);
}