package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.model.dto.PlayerResponseDTO;
import cat.itacademy.s05.t01.n01.model.dto.PlayerUpdateRequestDTO;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }

    /**
     * 📌 Endpoint para cambiar el nombre de un jugador.
     * @param playerId UUID del jugador.
     * @param request DTO con el nuevo nombre.
     * @return Mono<ResponseEntity<Void>> con 204 No Content si se actualizó correctamente.
     */
    @PutMapping("/{playerId}")
    public Mono<ResponseEntity<Void>> updatePlayerName(
            @PathVariable UUID playerId,
            @Valid @RequestBody PlayerUpdateRequestDTO request) {

        return playerService.updatePlayerName(playerId, request.getName())
                .thenReturn(ResponseEntity.noContent().build());
    }

    /**
     * 📌 Endpoint para obtener el ranking de jugadores basado en el número de victorias.
     * @return Flux<Player> con los jugadores ordenados por ranking.
     */
    @GetMapping("/ranking")
    public Mono<ResponseEntity<Flux<PlayerResponseDTO>>> getRanking() {
        return playerService.getRanking()
                .map(PlayerResponseDTO::new) //Fer_Develop: Convertimos `Player` en `PlayerResponseDTO`
                .collectList()
                .map(players -> players.isEmpty()
                        ? ResponseEntity.noContent().build() //Fer_Develop: Si la lista está vacía, devuelve `204 No Content`
                        : ResponseEntity.ok().body(Flux.fromIterable(players))
                );
    }
}