import cat.itacademy.s05.t01.n01.model.Game;
import reactor.core.publisher.Mono;

public interface GameService {

    /**
     * 📌 Crea una nueva partida de Blackjack.
     * @param playerName Nombre del jugador.
     * @return Mono<Game> con la partida creada.
     */
    Mono<Game> createGame(String playerName);
}