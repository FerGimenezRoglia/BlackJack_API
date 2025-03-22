package cat.itacademy.s05.t01.n01.service.impl;

import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameHistory;
import cat.itacademy.s05.t01.n01.model.enums.GameStatus;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.service.CardService;
import cat.itacademy.s05.t01.n01.service.GameHistoryService;
import cat.itacademy.s05.t01.n01.service.GamePlayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GamePlayServiceImpl implements GamePlayService {

    private final CardService cardService;
    private final GameRepository gameRepository;
    private final GameHistoryService gameHistoryService;

    @Autowired
    public GamePlayServiceImpl(CardService cardService, GameRepository gameRepository, GameHistoryService gameHistoryService) {
        this.cardService = cardService;
        this.gameRepository = gameRepository;
        this.gameHistoryService = gameHistoryService;
    }

    @Override
    public Mono<Game> handleHit(Game game) {
        Card newCard = cardService.drawSingleCard();
        game.getPlayerCards().add(newCard);

        int playerTotal = calculateHandValue(game.getPlayerCards());

        if (playerTotal > 21) {
            game.setStatus(GameStatus.FINISHED);
            game.setWinner("DEALER");
            return gameRepository.save(game);
        }

        // Guardamos el historial en MongoDB
        return gameRepository.save(game)
                .flatMap(savedGame -> saveFinalGameHistory(savedGame)
                        .thenReturn(savedGame));
    }

    private int calculateHandValue(List<Card> cards) {
        int total = 0;
        int aceCount = 0;

        for (Card card : cards) {
            switch (card.getRank()) {
                case TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN -> total += card.getRank().getValue();
                case JACK, QUEEN, KING -> total += 10;
                case ACE -> {
                    total += 11;
                    aceCount++;
                }
            }
        }

        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }

    @Override
    public Mono<Game> handleStand(Game game) {
        final int DEALER_MIN_SCORE = 17; //Definimos constante para claridad

        // 🔹 Copia de la lista de cartas del dealer para evitar mutabilidad dentro del loop
        List<Card> updatedDealerCards = new ArrayList<>(game.getDealerCards());

        // 🔹 Lógica del dealer: Juega su turno automáticamente hasta 17 o más
        while (calculateHandValue(updatedDealerCards) < DEALER_MIN_SCORE) {
            Card newCard = cardService.drawSingleCard();
            updatedDealerCards.add(newCard);
        }

        // 🔹 Calculamos los valores finales después del turno del dealer
        int playerTotal = calculateHandValue(game.getPlayerCards());
        int dealerTotal = calculateHandValue(updatedDealerCards);

        // 🔹 Determinamos el ganador
        String winner;
        if (dealerTotal > 21 || playerTotal > dealerTotal) {
            winner = "PLAYER";
        } else if (dealerTotal > playerTotal) {
            winner = "DEALER";
        } else {
            winner = "DRAW"; // Empate
        }

        // 🔹 Actualizamos el estado del juego de manera reactiva
        game.setDealerCards(updatedDealerCards);
        game.setWinner(winner);
        game.setStatus(GameStatus.FINISHED);

        // 🔹 Guardamos el historial en MongoDB
        return gameRepository.save(game)
                .flatMap(savedGame -> saveFinalGameHistory(savedGame)
                        .thenReturn(savedGame));
    }

    /**
     * Guarda la partida final en el historial de MongoDB.
     */
    private Mono<Void> saveFinalGameHistory(Game game) {
        GameHistory history = GameHistory.builder()
                .gameId(game.getId().toString())
                .playerId(null) // Si la partida finalizó, el playerId no es relevante
                .playerCards(gameHistoryService.convertToCardRecord(game.getPlayerCards()))
                .dealerCards(gameHistoryService.convertToCardRecord(game.getDealerCards()))
                .gameResult("FINISHED")
                .winnerId(game.getWinner().equals("PLAYER") ? game.getId().toString() : "DEALER")
                .winnerType(game.getWinner())
                .timestamp(LocalDateTime.now())
                .build();
        return gameHistoryService.saveGameHistory(history).then();
    }

}