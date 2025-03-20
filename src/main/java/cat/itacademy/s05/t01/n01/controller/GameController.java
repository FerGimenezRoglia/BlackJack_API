package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameHistory;
import cat.itacademy.s05.t01.n01.model.enums.GameStatus;
import cat.itacademy.s05.t01.n01.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 📌 Controlador para gestionar las partidas del Blackjack.
 */
@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * 📌 Crea una nueva partida.
     */
    @PostMapping("/new")
    public Mono<ResponseEntity<Game>> createGame(@RequestParam String playerName) {
        return gameService.createGame(playerName)
                .map(game -> ResponseEntity.ok().body(game));
    }

    /**
     * 📌 Obtiene los detalles de una partida por su ID.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Game>> getGameById(@PathVariable UUID id) {
        return gameService.getGameById(id)
                .map(ResponseEntity::ok);
    }

    /**
     * 📌 Realiza una jugada en una partida en curso.
     */
    @PostMapping("/{id}/play")
    public Mono<ResponseEntity<Game>> playGame(@PathVariable UUID id, @RequestParam String action) {
        return gameService.playGame(id, action)
                .map(ResponseEntity::ok);
    }

    /**
     * 📌 Elimina una partida por su ID.
     */
    @DeleteMapping("/{id}/delete")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable UUID id) {
        return gameService.deleteGame(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    /**
     * 📌 Obtiene todas las partidas según su estado.
     */
    @GetMapping("/status/{status}")
    public Flux<Game> getGamesByStatus(@PathVariable String status) {
        return gameService.getGamesByStatus(GameStatus.valueOf(status.toUpperCase()));
    }

    /**
     * 📌 Obtiene el historial de una partida desde MongoDB.
     */
    @GetMapping("/history/{id}")
    public Flux<GameHistory> getPlayerHistory(@PathVariable UUID playerId) {
        return gameService.getPlayerHistory(playerId);
    }
}