package cat.itacademy.s05.S05.service.impl;

import cat.itacademy.s05.S05.model.enums.GameState;
import cat.itacademy.s05.S05.model.enums.PlayerMove;
import cat.itacademy.s05.S05.model.enums.Rank;
import cat.itacademy.s05.S05.exception.domain.GameAlreadyEndedException;
import cat.itacademy.s05.S05.exception.domain.GameNotFoundException;
import cat.itacademy.s05.S05.exception.domain.GameNotInProgressException;
import cat.itacademy.s05.S05.exception.domain.InvalidMoveException;
import cat.itacademy.s05.S05.model.Card;
import cat.itacademy.s05.S05.model.Game;
import cat.itacademy.s05.S05.model.Hand;
import cat.itacademy.s05.S05.repository.GameRepository;
import cat.itacademy.s05.S05.service.DeckService;
import cat.itacademy.s05.S05.service.GameService;
import cat.itacademy.s05.S05.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final DeckService deckService;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, PlayerService playerService, DeckService deckService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.deckService = deckService;
    }

    @Override
    public Mono<Game> createGame(String playerName) {
        Game game = new Game();
        game.setPlayerName(playerName);
        game.setState(GameState.IN_PROGRESS);

        List<Card> deck = deckService.createShuffledDeck();
        game.setDeckRemainingCards(deck);
        game.setPlayerHandCards(new Hand());
        game.setDealerHandCards(new Hand());
        initializeHands(game);

        return gameRepository.save(game)
                .doOnSuccess(savedGame -> logger.info("Game created successfully for player: {}", savedGame.getPlayerName()));
    }

    private void initializeHands(Game game) {
        game.getPlayerHandCards().getCards().add(deckService.drawCard(game.getDeckRemainingCards()));
        game.getPlayerHandCards().getCards().add(deckService.drawCard(game.getDeckRemainingCards()));
        game.getDealerHandCards().getCards().add(deckService.drawCard(game.getDeckRemainingCards()));
        game.getDealerHandCards().getCards().add(deckService.drawCard(game.getDeckRemainingCards()));
        logger.info("Initial cards dealt to player and dealer.");
    }

    @Override
    public Flux<Game> getAllGames() {
        logger.info("Fetching all games from the database.");
        return gameRepository.findAll();
    }

    @Override
    public Mono<Game> getGame(String gameId) {
        logger.info("Fetching game with ID: {}", gameId);
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with ID: " + gameId)));
    }

    @Override
    public Mono<Game> playMove(String gameId, String move) {
        return validateMove(move)
                .flatMap(playerMove -> gameRepository.findById(gameId)
                        .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with ID: " + gameId)))
                        .flatMap(game -> validateGameState(game)
                                .flatMap(validatedGame -> processMove(validatedGame, playerMove))));
    }

    private Mono<PlayerMove> validateMove(String move) {
        return PlayerMove.fromString(move)
                .map(Mono::just)
                .orElseGet(() -> Mono.error(new InvalidMoveException("Invalid move: " + move)));
    }

    private Mono<Game> validateGameState(Game game) {
        if (game.getState() == GameState.FINISHED) {
            return Mono.error(new GameAlreadyEndedException("Game already ended."));
        }
        if (game.getState() != GameState.IN_PROGRESS) {
            return Mono.error(new GameNotInProgressException("Cannot play move, game is not in progress."));
        }
        return Mono.just(game);
    }

    private Mono<Game> processMove(Game game, PlayerMove move) {
        switch (move) {
            case HIT -> handleHitMove(game);
            case STAND -> handleStandMove(game);
            default -> throw new InvalidMoveException("Invalid move: " + move);
        }

        if (game.getState() == GameState.IN_PROGRESS) {
            return gameRepository.save(game);
        }

        return endGame(game);
    }

    private void handleHitMove(Game game) {
        game.getPlayerHandCards().getCards().add(deckService.drawCard(game.getDeckRemainingCards()));
        logger.info("Player {} hits. New hand total: {}", game.getPlayerName(), calculateHandTotal(game.getPlayerHandCards()));

        if (calculateHandTotal(game.getPlayerHandCards()) >= 21) {
            game.setState(GameState.FINISHED);
            determineWinner(game);
            logger.info("Game ended after hit move. Player total: {}. Winner: {}", calculateHandTotal(game.getPlayerHandCards()), game.getWinner());
        }
    }

    private void handleStandMove(Game game) {
        dealerTurn(game);
        game.setState(GameState.FINISHED);
        determineWinner(game);
        logger.info("Game ended after stand move. Winner: {}", game.getWinner());
    }

    private void dealerTurn(Game game) {
        logger.info("Dealer's turn started. Initial total: {}", calculateHandTotal(game.getDealerHandCards()));

        while (calculateHandTotal(game.getDealerHandCards()) < 17 && !game.getDeckRemainingCards().isEmpty()) {
            game.getDealerHandCards().getCards().add(deckService.drawCard(game.getDeckRemainingCards()));
            logger.info("Dealer drew a card. New total: {}", calculateHandTotal(game.getDealerHandCards()));
        }

        logger.info("Dealer's turn ended. Final total: {}", calculateHandTotal(game.getDealerHandCards()));
    }

    private int calculateHandTotal(Hand hand) {
        int total = 0;
        int aceCount = 0;
        for (Card card : hand.getCards()) {
            total += card.getRank().getValue();
            if (card.getRank() == Rank.ACE) aceCount++;
        }
        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }
        return total;
    }

    private void determineWinner(Game game) {
        int playerScore = calculateHandTotal(game.getPlayerHandCards());
        int dealerScore = calculateHandTotal(game.getDealerHandCards());

        if (playerScore > 21) {
            game.setWinner("dealer");
        } else if (dealerScore > 21 || playerScore > dealerScore) {
            game.setWinner("player");
        } else if (dealerScore > playerScore) {
            game.setWinner("dealer");
        } else {
            game.setWinner("tie");
        }
        logger.info("Game result determined. Winner: {}", game.getWinner());
    }

    private Mono<Game> endGame(Game game) {
        if (game.getState() != GameState.FINISHED) {
            dealerTurn(game);
            determineWinner(game);
            game.setState(GameState.FINISHED);
            logger.info("Game ended. Winner determined: {}", game.getWinner());
        }

        return playerService.updatePlayerStats(game)
                .then(gameRepository.save(game));
    }

    @Override
    public Mono<Void> deleteGame(String gameId) {
        logger.info("Deleting game with ID: {}", gameId);
        return gameRepository.deleteById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with ID: " + gameId)))
                .flatMap(game -> gameRepository.deleteById(gameId))
                .doOnSuccess(unused -> logger.info("Game with ID {} deleted successfully.", gameId));

    }
}
