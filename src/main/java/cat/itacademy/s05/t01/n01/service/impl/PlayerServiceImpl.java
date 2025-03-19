package cat.itacademy.s05.t01.n01.service.impl;

import cat.itacademy.s05.t01.n01.exception.DuplicatePlayerException;
import cat.itacademy.s05.t01.n01.exception.InvalidActionException;
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
        if (newName == null || newName.isBlank()) {
            return Mono.error(new InvalidActionException("El nombre del jugador no puede estar vacío."));
        }

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

    /**
     * Obtiene un jugador por nombre e ID si existe, o lo crea si no existe.
     * @param playerName Nombre del jugador.
     * @return Mono con el jugador encontrado o creado.
     */
    @Override
    public Mono<Player> getOrCreatePlayer(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            return Mono.error(new InvalidActionException("El nombre del jugador no puede estar vacío"));
        }
        return playerRepository.findByName(playerName)
                .flatMap(existingPlayer -> Mono.<Player>error(new DuplicatePlayerException("El jugador '" + playerName + "' ya existe.")))
                .switchIfEmpty(createAndSavePlayer(playerName));
    }

    /**
     * 📌 Método auxiliar para crear y guardar un nuevo jugador
     */
    private Mono<Player> createAndSavePlayer(String playerName) {
        Player newPlayer = new Player(UUID.randomUUID(), playerName, 0, 0);
        return playerRepository.save(newPlayer);
    }
}