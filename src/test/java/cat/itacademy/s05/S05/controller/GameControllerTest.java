package cat.itacademy.s05.S05.controller;

import cat.itacademy.s05.S05.exception.domain.GameNotFoundException;
import cat.itacademy.s05.S05.model.dto.CreateGameRequest;
import cat.itacademy.s05.S05.model.dto.GameResponse;
import cat.itacademy.s05.S05.model.dto.PlayerMoveRequest;
import cat.itacademy.s05.S05.model.enums.GameState;
import cat.itacademy.s05.S05.model.Game;
import cat.itacademy.s05.S05.model.Hand;
import cat.itacademy.s05.S05.service.GameService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    private Game mockGame;
    private GameResponse mockGameResponse;

    @BeforeEach
    void setUp() {
        // Create a mock game
        mockGame = new Game();
        mockGame.setId("game123");
        mockGame.setPlayerName("TestPlayer");
        mockGame.setState(GameState.IN_PROGRESS);
        mockGame.setPlayerHandCards(new Hand());
        mockGame.setDealerHandCards(new Hand());
        mockGame.setDeckRemainingCards(new ArrayList<>());

        // Create a mock game response
        mockGameResponse = new GameResponse(mockGame);
    }

    @Test
    void createGame_ShouldReturnCreatedGameResponse() {
        CreateGameRequest request = new CreateGameRequest();
        request.setPlayerName("TestPlayer");

        when(gameService.createGame(anyString())).thenReturn(Mono.just(mockGame));

        Mono<ResponseEntity<GameResponse>> result = gameController.createGame(request);

        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
                    assertEquals("TestPlayer", responseEntity.getBody().getPlayerName());
                    assertEquals("game123", responseEntity.getBody().getId());
                    assertEquals(GameState.IN_PROGRESS, responseEntity.getBody().getState());
                })
                .verifyComplete();
    }

    @Test
    void getAllGames_WithGames_ShouldReturnOkWithGames() {
        List<Game> games = List.of(mockGame);
        when(gameService.getAllGames()).thenReturn(Flux.fromIterable(games));

        Mono<ResponseEntity<Flux<GameResponse>>> result = gameController.getAllGames();

        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    StepVerifier.create(responseEntity.getBody())
                            .assertNext(game -> assertEquals("TestPlayer", game.getPlayerName()))
                            .verifyComplete();
                })
                .verifyComplete();
    }

    @Test
    void getAllGames_WithNoGames_ShouldReturnNoContent() {
        when(gameService.getAllGames()).thenReturn(Flux.empty());

        Mono<ResponseEntity<Flux<GameResponse>>> result = gameController.getAllGames();

        StepVerifier.create(result)
                .assertNext(responseEntity -> assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode()))
                .verifyComplete();
    }

    @Test
    void getGame_ExistingGame_ShouldReturnGame() {
        when(gameService.getGame(anyString())).thenReturn(Mono.just(mockGame));

        Mono<ResponseEntity<GameResponse>> result = gameController.getGame("game123");

        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertEquals("TestPlayer", responseEntity.getBody().getPlayerName());
                })
                .verifyComplete();
    }

    @Test
    void getGame_NonExistingGame_ShouldReturnNotFound() {
        when(gameService.getGame(anyString())).thenReturn(Mono.empty());

        Mono<ResponseEntity<GameResponse>> result = gameController.getGame("nonExistentGame");

        StepVerifier.create(result)
                .assertNext(responseEntity -> assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()))
                .verifyComplete();
    }

    @Test
    void playMove_ValidMove_ShouldReturnUpdatedGame() {
        PlayerMoveRequest request = new PlayerMoveRequest();
        request.setMove("Hit");

        when(gameService.playMove(anyString(), anyString())).thenReturn(Mono.just(mockGame));

        Mono<ResponseEntity<GameResponse>> result = gameController.playMove("game123", request);

        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertEquals("TestPlayer", responseEntity.getBody().getPlayerName());
                })
                .verifyComplete();
    }

    @Test
    void deleteGame_ShouldReturnNoContent() {
        when(gameService.deleteGame("game123")).thenReturn(Mono.empty());

        StepVerifier.create(gameController.deleteGame("game123"))
                .expectNext(ResponseEntity.noContent().build())
                .verifyComplete();
    }

    @Test
    void deleteGame_ShouldReturnGameNotFoundException() {
        when(gameService.deleteGame("game123"))
                .thenReturn(Mono.error(new GameNotFoundException("Game not found")));

        StepVerifier.create(gameController.deleteGame("game123"))
                .expectError(GameNotFoundException.class)
                .verify();
    }
}
