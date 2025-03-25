package cat.itacademy.s05.S05.controller;

import cat.itacademy.s05.S05.model.dto.CreateGameRequest;
import cat.itacademy.s05.S05.model.dto.GameResponse;
import cat.itacademy.s05.S05.model.dto.PlayerMoveRequest;
import cat.itacademy.s05.S05.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Game API", description = "Endpoints for managing games")
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(
            summary = "Create a new game",
            description = "Creates a game for a player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Player already exists"),
    })
    @PostMapping
    public Mono<ResponseEntity<GameResponse>> createGame(@Valid @RequestBody CreateGameRequest request) {
        return gameService.createGame(request.getPlayerName())
                .map(game -> ResponseEntity.status(HttpStatus.CREATED).body(new GameResponse(game)));
    }

    @Operation(
            summary = "Get all games",
            description = "Retrieves all existing games")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Games retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No games found")
    })
    @GetMapping
    public Mono<ResponseEntity<Flux<GameResponse>>> getAllGames() {
        return gameService.getAllGames()
                .map(GameResponse::new)
                .collectList()
                .map(games -> games.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok().body(Flux.fromIterable(games))
                );
    }

    @Operation(
            summary = "Get a game by ID",
            description = "Retrieves a specific game by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game found"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<GameResponse>> getGame(@PathVariable String id) {
        return gameService.getGame(id)
                .map(game -> ResponseEntity.ok().body(new GameResponse(game)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Play a move in a game",
            description = "Processes a player's move in a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Move processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid move or input"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @PostMapping("/{id}/moves")
    public Mono<ResponseEntity<GameResponse>> playMove(@PathVariable String id, @Valid @RequestBody PlayerMoveRequest request) {
        return gameService.playMove(id, request.getMove())
                .map(game -> ResponseEntity.ok().body(new GameResponse(game)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a game",
            description = "Deletes a game by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Game deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}