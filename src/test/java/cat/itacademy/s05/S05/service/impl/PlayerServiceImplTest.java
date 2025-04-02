package cat.itacademy.s05.S05.service.impl;

import cat.itacademy.s05.S05.model.enums.GameState;
import cat.itacademy.s05.S05.exception.domain.PlayerNotFoundException;
import cat.itacademy.s05.S05.exception.domain.RankingIsEmptyException;
import cat.itacademy.s05.S05.model.Game;
import cat.itacademy.s05.S05.model.Hand;
import cat.itacademy.s05.S05.model.Player;
import cat.itacademy.s05.S05.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private Player mockPlayer;
    private Game mockGame;

    @BeforeEach
    void setUp() {
        mockPlayer = new Player(1L, "TestPlayer", 5);

        mockGame = new Game();
        mockGame.setId("game123");
        mockGame.setPlayerName("TestPlayer");
        mockGame.setState(GameState.FINISHED);
        mockGame.setWinner("player");
        mockGame.setPlayerHandCards(new Hand());
        mockGame.setDealerHandCards(new Hand());
        mockGame.setDeckRemainingCards(new ArrayList<>());
    }

    @Test
    void updatePlayerName_ExistingPlayer_ShouldReturnUpdatedPlayer() {
        when(playerRepository.findById(anyLong())).thenReturn(Mono.just(mockPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(mockPlayer));

        Mono<Player> result = playerService.updatePlayerName(1L, "NewName");

        StepVerifier.create(result)
                .expectNext(mockPlayer)
                .verifyComplete();
    }

    @Test
    void updatePlayerName_NonExistingPlayer_ShouldThrowException() {
        when(playerRepository.findById(anyLong())).thenReturn(Mono.empty());

        Mono<Player> result = playerService.updatePlayerName(999L, "NewName");

        StepVerifier.create(result)
                .expectError(PlayerNotFoundException.class)
                .verify();
    }

    @Test
    void getRanking_WithPlayers_ShouldReturnPlayers() {
        when(playerRepository.findTop10ByOrderByWinsDesc()).thenReturn(Flux.just(mockPlayer));

        Flux<Player> result = playerService.getRanking();

        StepVerifier.create(result)
                .expectNext(mockPlayer)
                .verifyComplete();
    }

    @Test
    void getRanking_WithNoPlayers_ShouldThrowException() {
        when(playerRepository.findTop10ByOrderByWinsDesc()).thenReturn(Flux.empty());

        Flux<Player> result = playerService.getRanking();

        StepVerifier.create(result)
                .expectError(RankingIsEmptyException.class)
                .verify();
    }

    @Test
    void findByName_ExistingPlayer_ShouldReturnPlayer() {
        when(playerRepository.findByName(anyString())).thenReturn(Mono.just(mockPlayer));

        Mono<Player> result = playerService.findByName("TestPlayer");

        StepVerifier.create(result)
                .expectNext(mockPlayer)
                .verifyComplete();
    }

    @Test
    void findByName_NonExistingPlayer_ShouldCreateAndReturnNewPlayer() {
        when(playerRepository.findByName(anyString())).thenReturn(Mono.empty());
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(mockPlayer));

        Mono<Player> result = playerService.findByName("NewPlayer");

        StepVerifier.create(result)
                .expectNext(mockPlayer)
                .verifyComplete();
    }

    @Test
    void save_ShouldReturnSavedPlayer() {
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(mockPlayer));

        Mono<Player> result = playerService.save(mockPlayer);

        StepVerifier.create(result)
                .expectNext(mockPlayer)
                .verifyComplete();
    }

    @Test
    void updatePlayerStats_PlayerWins_ShouldUpdatePlayerStats() {
        when(playerRepository.findByName(anyString())).thenReturn(Mono.just(mockPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(mockPlayer));

        Mono<Void> result = playerService.updatePlayerStats(mockGame);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void updatePlayerStats_PlayerNotFound_ShouldCreatePlayer() {
        when(playerRepository.findByName(anyString())).thenReturn(Mono.empty());
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(mockPlayer));

        Mono<Void> result = playerService.updatePlayerStats(mockGame);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void updatePlayerStats_PlayerDoesNotWin_ShouldNotUpdateStats() {
        mockGame.setWinner("dealer");
        when(playerRepository.findByName(anyString())).thenReturn(Mono.just(mockPlayer));

        Mono<Void> result = playerService.updatePlayerStats(mockGame);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
