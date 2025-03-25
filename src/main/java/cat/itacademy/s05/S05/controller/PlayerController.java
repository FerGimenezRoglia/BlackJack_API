package cat.itacademy.s05.S05.controller;

import cat.itacademy.s05.S05.model.dto.PlayerResponse;
import cat.itacademy.s05.S05.model.dto.UpdatePlayerNameRequest;
import cat.itacademy.s05.S05.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Player API", description = "Endpoints for managing players and rankings")
@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(
            summary = "Update player name",
            description = "Changes the name of an existing player"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Player name updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })    @PutMapping("/{playerId}")
    public Mono<ResponseEntity<Void>> updatePlayerName(@PathVariable Long playerId, @Valid @RequestBody UpdatePlayerNameRequest request) {
        return playerService.updatePlayerName(playerId, request.getPlayerName())
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Operation(
            summary = "Get player ranking",
            description = "Retrieves the top players by wins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of players retrieved"),
            @ApiResponse(responseCode = "204", description = "No players found"),
    })
    @GetMapping("/ranking")
    public Mono<ResponseEntity<Flux<PlayerResponse>>> getRanking() {
        return playerService.getRanking()
                .map(PlayerResponse::new)
                .collectList()
                .map(players -> players.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok().body(Flux.fromIterable(players))
                );
    }
}