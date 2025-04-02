package cat.itacademy.s05.S05.service.impl;

import cat.itacademy.s05.S05.model.enums.GameState;
import cat.itacademy.s05.S05.model.enums.Rank;
import cat.itacademy.s05.S05.model.enums.Suit;
import cat.itacademy.s05.S05.exception.domain.GameNotFoundException;
import cat.itacademy.s05.S05.exception.domain.InvalidMoveException;
import cat.itacademy.s05.S05.model.Card;
import cat.itacademy.s05.S05.model.Game;
import cat.itacademy.s05.S05.model.Hand;
import cat.itacademy.s05.S05.repository.GameRepository;
import cat.itacademy.s05.S05.service.DeckService;
import cat.itacademy.s05.S05.service.PlayerService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private DeckService deckService;

    @InjectMocks
    private GameServiceImpl gameService;

    private Game mockGame;
    private List<Card> mockDeck;

    @BeforeEach
    void setUp() {
        // Create mock cards
        Card playerCard1 = new Card(Rank.KING, Suit.HEARTS);
        Card playerCard2 = new Card(Rank.QUEEN, Suit.SPADES);
        Card dealerCard1 = new Card(Rank.NINE, Suit.CLUBS);
        Card dealerCard2 = new Card(Rank.EIGHT, Suit.DIAMONDS);
        Card deckCard = new Card(Rank.SEVEN, Suit.HEARTS);

        // Create mock hands
        Hand playerHand = new Hand();
        playerHand.getCards().add(playerCard1);
        playerHand.getCards().add(playerCard2);

        Hand dealerHand = new Hand();
        dealerHand.getCards().add(dealerCard1);
        dealerHand.getCards().add(dealerCard2);

        // Create mock deck
        mockDeck = new ArrayList<>();
        mockDeck.add(deckCard);

        // Create mock game
        mockGame = new Game();
        mockGame.setId("game123");
        mockGame.setPlayerName("TestPlayer");
        mockGame.setState(GameState.IN_PROGRESS);
        mockGame.setPlayerHandCards(playerHand);
        mockGame.setDealerHandCards(dealerHand);
        mockGame.setDeckRemainingCards(mockDeck);
    }

    @Test
    void createGame_ShouldReturnNewGame() {
        when(deckService.createShuffledDeck()).thenReturn(mockDeck);
        when(deckService.drawCard(any())).thenReturn(new Card(Rank.ACE, Suit.HEARTS));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(mockGame));

        Mono<Game> result = gameService.createGame("TestPlayer");

        StepVerifier.create(result)
                .expectNextMatches(game ->
                        game.getId().equals("game123") &&
                                game.getPlayerName().equals("TestPlayer") &&
                                game.getState() == GameState.IN_PROGRESS
                )
                .verifyComplete();
    }

    @Test
    void getAllGames_ShouldReturnAllGames() {
        when(gameRepository.findAll()).thenReturn(Flux.just(mockGame));

        Flux<Game> result = gameService.getAllGames();

        StepVerifier.create(result)
                .expectNextMatches(game -> game.getId().equals("game123"))
                .verifyComplete();
    }

    @Test
    void getGame_ExistingGame_ShouldReturnGame() {
        when(gameRepository.findById(anyString())).thenReturn(Mono.just(mockGame));

        Mono<Game> result = gameService.getGame("game123");

        StepVerifier.create(result)
                .expectNextMatches(game -> game.getId().equals("game123"))
                .verifyComplete();
    }

    @Test
    void getGame_NonExistingGame_ShouldThrowException() {
        when(gameRepository.findById(anyString())).thenReturn(Mono.empty());

        Mono<Game> result = gameService.getGame("nonExistentGame");

        StepVerifier.create(result)
                .expectError(GameNotFoundException.class)
                .verify();
    }

    @Test
    void playMove_ValidHitMove_ShouldReturnUpdatedGame() {
        Card newCard = new Card(Rank.FIVE, Suit.CLUBS);
        when(gameRepository.findById(anyString())).thenReturn(Mono.just(mockGame));
        when(deckService.drawCard(any())).thenReturn(newCard);
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(mockGame));
        when(playerService.updatePlayerStats(any(Game.class))).thenReturn(Mono.empty());

        Mono<Game> result = gameService.playMove("game123", "Hit");

        StepVerifier.create(result)
                .expectNextMatches(game -> game.getId().equals("game123"))
                .verifyComplete();
    }

    @Test
    void playMove_ValidStandMove_ShouldReturnGameWithWinner() {
        when(gameRepository.findById(anyString())).thenReturn(Mono.just(mockGame));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(mockGame));
        when(playerService.updatePlayerStats(any(Game.class))).thenReturn(Mono.empty());

        Mono<Game> result = gameService.playMove("game123", "Stand");

        StepVerifier.create(result)
                .expectNextMatches(game ->
                        game.getId().equals("game123") &&
                                game.getState() == GameState.FINISHED
                )
                .verifyComplete();
    }

    @Test
    void playMove_InvalidMove_ShouldThrowException() {
        Mono<Game> result = gameService.playMove("game123", "InvalidMove");

        StepVerifier.create(result)
                .expectError(InvalidMoveException.class)
                .verify();
    }

    @Test
    void deleteGame_ExistingGame_ShouldCompleteWithoutError() {
        when(gameRepository.findById(anyString())).thenReturn(Mono.just(mockGame));
        when(gameRepository.deleteById(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = gameService.deleteGame("game123");

        StepVerifier.create(result)
                .verifyComplete();
    }
}
