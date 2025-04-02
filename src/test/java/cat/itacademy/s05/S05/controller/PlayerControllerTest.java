package cat.itacademy.s05.S05.controller;

import cat.itacademy.s05.S05.model.dto.PlayerResponse;
import cat.itacademy.s05.S05.model.dto.UpdatePlayerNameRequest;
import cat.itacademy.s05.S05.model.Player;
import cat.itacademy.s05.S05.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        mockPlayer = new Player(1L, "TestPlayer", 5);
    }

    @Test
    void updatePlayerName_ShouldReturnNoContent() {
        UpdatePlayerNameRequest request = new UpdatePlayerNameRequest();
        request.setPlayerName("NewPlayerName");

        when(playerService.updatePlayerName(anyLong(), anyString())).thenReturn(Mono.just(mockPlayer));

        Mono<ResponseEntity<Void>> result = playerController.updatePlayerName(1L, request);

        StepVerifier.create(result)
                .assertNext(responseEntity -> assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode()))
                .verifyComplete();
    }

    @Test
    void getRanking_WithPlayers_ShouldReturnOkWithPlayers() {
        List<Player> players = List.of(mockPlayer);
        when(playerService.getRanking()).thenReturn(Flux.fromIterable(players));

        Mono<ResponseEntity<Flux<PlayerResponse>>> result = playerController.getRanking();

        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    StepVerifier.create(responseEntity.getBody())
                            .assertNext(player -> {
                                assertEquals(1L, player.getId());
                                assertEquals("TestPlayer", player.getName());
                                assertEquals(5, player.getWins());
                            })
                            .verifyComplete();
                })
                .verifyComplete();
    }

    @Test
    void getRanking_WithNoPlayers_ShouldReturnNoContent() {
        when(playerService.getRanking()).thenReturn(Flux.empty());

        Mono<ResponseEntity<Flux<PlayerResponse>>> result = playerController.getRanking();

        StepVerifier.create(result)
                .assertNext(responseEntity -> assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode()))
                .verifyComplete();
    }
}