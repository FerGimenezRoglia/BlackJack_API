package cat.itacademy.s05.t01.n01.service.impl;

import cat.itacademy.s05.t01.n01.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @author Fer_Develop
 * Implementación del servicio de jugadores.
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    /**
     * Constructor con inyección de dependencia.
     * @param playerRepository Repositorio de jugadores.
     */
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Cambia el nombre de un jugador existente.
     * @param playerId UUID del jugador.
     * @param newName Nuevo nombre del jugador.
     * @return Mono vacío cuando la actualización se complete.
     */
    @Override
    public Mono<Void> updatePlayerName(UUID playerId, String newName) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Jugador no encontrado con ID: " + playerId)))
                .flatMap(player -> {
                    player.setName(newName);
                    return playerRepository.save(player);
                })
                .then();
    }

    /**
     * Obtiene el ranking de jugadores basado en el número de victorias.
     * @return Flux con los jugadores ordenados por ranking.
     */
    @Override
    public Flux<Player> getRanking() {
        return playerRepository.findTop10ByOrderByTotalWinsDesc();
    }
}