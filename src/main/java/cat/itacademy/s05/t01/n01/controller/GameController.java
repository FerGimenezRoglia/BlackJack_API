package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameHistory;
import cat.itacademy.s05.t01.n01.model.enums.GameStatus;
import cat.itacademy.s05.t01.n01.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/game")
@Tag(name = "Game Controller", description = "Gestión de partidas de Blackjack")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Crear una nueva partida", description = "Inicia una nueva partida de Blackjack con un jugador y asigna cartas iniciales.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud")
    })
    @PostMapping("/new")
    public Mono<ResponseEntity<Game>> createGame(@RequestParam String playerName) {
        return gameService.createGame(playerName)
                .map(game -> ResponseEntity.status(201).body(game));
    }

    @Operation(summary = "Obtener detalles de una partida", description = "Recupera los datos de una partida específica según su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles de la partida obtenidos"),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Game>> getGameById(@PathVariable @Valid UUID id) {
        return gameService.getGameById(id)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Realizar una jugada en la partida", description = "El jugador puede realizar una acción en el juego. Las acciones permitidas son: 'HIT' (pedir carta) o 'STAND' (plantarse).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugada procesada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Acción inválida o partida finalizada"),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    })
    @PostMapping("/{id}/play")
    public Mono<ResponseEntity<Game>> playGame(@PathVariable @Valid UUID id, @RequestParam String action) {
        return gameService.playGame(id, action)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Eliminar una partida", description = "Permite eliminar una partida activa mediante su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Partida eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    })
    @DeleteMapping("/{id}/delete")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable @Valid UUID id) {
        return gameService.deleteGame(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Obtener partidas por estado", description = "Lista todas las partidas según su estado (IN_PROGRESS o FINISHED).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de partidas obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Estado inválido")
    })
    @GetMapping("/status/{status}")
    public Flux<Game> getGamesByStatus(@PathVariable String status) {
        return gameService.getGamesByStatus(GameStatus.valueOf(status.toUpperCase()));
    }

    @Operation(summary = "Obtener el historial de partidas de un jugador", description = "Recupera el historial de partidas de un jugador desde MongoDB.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron partidas en el historial del jugador")
    })
    @GetMapping("/history/{playerId}")
    public Flux<GameHistory> getPlayerHistory(@PathVariable UUID playerId) {
        return gameService.getPlayerHistory(playerId.toString());
    }
}