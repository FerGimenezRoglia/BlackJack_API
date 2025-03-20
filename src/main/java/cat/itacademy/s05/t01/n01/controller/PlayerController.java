package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.model.dto.PlayerResponseDTO;
import cat.itacademy.s05.t01.n01.model.dto.PlayerUpdateRequestDTO;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/player")
@Tag(name = "Player Controller", description = "Gestión de jugadores en el Blackjack")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }


    @Operation(summary = "Actualizar el nombre de un jugador", description = "Permite modificar el nombre de un jugador registrado en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Nombre actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Formato de UUID inválido o datos incorrectos"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    @PutMapping("/{playerId}")
    public Mono<ResponseEntity<Void>> updatePlayerName(
            @PathVariable @Valid UUID playerId,
            @Valid @RequestBody PlayerUpdateRequestDTO request) {

        return playerService.updatePlayerName(playerId, request.getName())
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Obtener ranking de jugadores", description = "Devuelve el ranking de los jugadores basado en el número de victorias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de jugadores obtenida correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay jugadores en el ranking")
    })
    @GetMapping("/ranking")
    public Mono<ResponseEntity<Flux<PlayerResponseDTO>>> getRanking() {
        return playerService.getRanking()
                .map(PlayerResponseDTO::new) //Fer_Develop: Convertimos `Player` en `PlayerResponseDTO`
                .collectList()
                .map(players -> players.isEmpty()
                        ? ResponseEntity.noContent().build() //Fer_Develop: Si la lista está vacía, devuelve `204 No Content`
                        : ResponseEntity.ok(Flux.fromIterable(players))
                );
    }
}