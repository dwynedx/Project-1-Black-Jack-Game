package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlackjackGame extends Game {
    private static final double STARTING_BALANCE = 100.00;
    private static final int RESHUFFLE_LIMIT = 15;

    private Deck deck;
    private final Dealer dealer;
    private BlackjackPlayer player;
    private final ConsoleBlackjackView view;
    private boolean roundActive;
    private final List<Result> handResults;
    private boolean currentRoundWasSplit;

    public BlackjackGame(String name) {
        this(name, new Deck(), new ConsoleBlackjackView(new Scanner(System.in), System.out));
    }

    /**
     * Dependency-injection constructor used for determining tests and demos.
     */
    public BlackjackGame(String name, Deck deck, ConsoleBlackjackView view) {
        super(name);
        if (deck == null || view == null) {
            throw new IllegalArgumentException("Deck and view are required.");
        }
        this.deck = deck;
        this.view = view;
        this.dealer = new Dealer("Dealer", "D1");
        this.handResults = new ArrayList<Result>();
    }

    @Override
    public void play() {
        view.showTitle(getGameName());
        String playerName = view.askPlayerName();
        player = new BlackjackPlayer(playerName, "P1", STARTING_BALANCE);

        ArrayList<Player> participants = new ArrayList<Player>();
        participants.add(player);
        participants.add(dealer);
        setPlayers(participants);

        startGame();
    }

    /**
     * Repeats rounds until the player cashes out or loses the full balance.
     */
    public void startGame() {
        while (!gameOver()) {
            startRound();
            if (gameOver()) {
                break;
            }
            if (!view.askPlayAgain()) {
                cashOut();
                return;
            }
            view.showBlankLine();
        }

        view.showMessage("GAME OVER - your balance reached $0.00.");
        cashOut();
    }

    /**
     * Runs one complete round from wagering through settlement.
     */
    public void startRound() {
        if (deck.cardsRemaining() < RESHUFFLE_LIMIT) {
            deck.reset();
            view.showMessage("The deck was reshuffled.");
        }

        roundActive = true;
        currentRoundWasSplit = false;
        handResults.clear();
        player.prepareForRound();
        dealer.clearHands();
        dealer.hideSecondCard();

        double bet = view.askBet(player.getBalance());
        player.placeBet(bet);
        dealInitialCards();

        view.showBlankLine();
        view.showDealerHand(dealer, false);
        view.showPlayerHand(player, 0);

        if (player.getHand(0).isBlackjack() || dealer.getHand(0).isBlackjack()) {
            dealer.revealHiddenCard();
            view.showDealerHand(dealer, true);
            Result result = compareHands(player.getHand(0));
            handResults.add(result);
            settleHand(result, false);
            declareWinner();
            roundActive = false;
            return;
        }

        playerTurn();
        if (!allPlayerHandsBust()) {
            dealerTurn();
        } else {
            dealer.revealHiddenCard();
            view.showDealerHand(dealer, true);
        }

        for (int handIndex = 0; handIndex < player.getHandCount(); handIndex++) {
            Result result = compareHands(player.getHand(handIndex));
            handResults.add(result);
            settleHand(result, currentRoundWasSplit);
        }

        declareWinner();
        roundActive = false;
    }

    /**
     * Deals two cards to each participant in alternating order.
     */
    public void dealInitialCards() {
        player.receiveCard(deck.dealCard(), 0);
        dealer.receiveCard(deck.dealCard(), 0);
        player.receiveCard(deck.dealCard(), 0);
        dealer.receiveCard(deck.dealCard(), 0);
    }

    /**
     * Accepts hit, stand, and split actions for every active player hand.
     */
    public void playerTurn() {
        for (int handIndex = 0; handIndex < player.getHandCount(); handIndex++) {
            boolean handFinished = false;

            while (!handFinished) {
                Hand hand = player.getHand(handIndex);
                view.showBlankLine();
                view.showPlayerHand(player, handIndex);

                if (hand.isBust()) {
                    view.showMessage("Hand " + (handIndex + 1) + " is over 21.");
                    break;
                }
                if (hand.calculateValue() == 21) {
                    view.showMessage("Hand " + (handIndex + 1) + " has 21 and automatically stands.");
                    player.stand(handIndex);
                    break;
                }

                boolean splitAvailable = player.canSplit(handIndex);
                int action = view.askAction(splitAvailable);

                if (action == 1) {
                    BlackjackCard newCard = deck.dealCard();
                    player.receiveCard(newCard, handIndex);
                    view.showMessage("Card dealt: " + newCard);
                } else if (action == 2) {
                    player.stand(handIndex);
                    handFinished = true;
                } else if (action == 3) {
                    if (player.split(deck, handIndex)) {
                        currentRoundWasSplit = true;
                        view.showMessage("Pair split into two hands. An equal second bet was placed.");
                        view.showPlayerHand(player, 0);
                        view.showPlayerHand(player, 1);
                    } else {
                        view.showMessage("The hand could not be split.");
                    }
                }
            }
        }
    }

    /**
     * Reveals the dealer's hidden card and draws until the hand is at least 17.
     */
    public void dealerTurn() {
        dealer.revealHiddenCard();
        view.showBlankLine();
        view.showMessage("Dealer reveals the hidden card.");
        view.showDealerHand(dealer, true);

        while (dealer.shouldHit()) {
            BlackjackCard card = deck.dealCard();
            dealer.receiveCard(card, 0);
            view.showMessage("Dealer hits: " + card);
            view.showDealerHand(dealer, true);
        }

        if (dealer.getHand(0).isBust()) {
            view.showMessage("Dealer busts.");
        } else {
            view.showMessage("Dealer stands on " + dealer.getHandValue(0) + ".");
        }
    }

    public Result compareHands() {
        return compareHands(player.getHand(0));
    }

    /**
     * Determines the result for one player hand.
     */
    public Result compareHands(Hand playerHand) {
        Hand dealerHand = dealer.getHand(0);

        if (playerHand.isBust()) {
            return Result.PLAYER_BUST;
        }
        if (dealerHand.isBust()) {
            return Result.DEALER_BUST;
        }
        if (!currentRoundWasSplit && playerHand.isBlackjack() && dealerHand.isBlackjack()) {
            return Result.PUSH;
        }
        if (!currentRoundWasSplit && playerHand.isBlackjack()) {
            return Result.BLACKJACK;
        }
        if (dealerHand.isBlackjack()) {
            return Result.DEALER_WIN;
        }

        int playerValue = playerHand.calculateValue();
        int dealerValue = dealerHand.calculateValue();
        if (playerValue > dealerValue) {
            return Result.PLAYER_WIN;
        }
        if (playerValue < dealerValue) {
            return Result.DEALER_WIN;
        }
        return Result.PUSH;
    }

    /**
     * Displays each hand result and the updated balance.
     */
    @Override
    public void declareWinner() {
        view.showBlankLine();
        for (int index = 0; index < handResults.size(); index++) {
            view.showMessage("Hand " + (index + 1) + " result: " + handResults.get(index).getMessage());
        }
        view.showMessage(String.format("Current balance: $%.2f", player.getBalance()));
    }

    public boolean gameOver() {
        return player != null && player.getBalance() <= 0.0001;
    }

    /**
     * Displays the final balance and profit or loss relative to the starting $100 balance.
     */
    public void cashOut() {
        double difference = player.getBalance() - STARTING_BALANCE;
        view.showBlankLine();
        view.showMessage(String.format("Final balance: $%.2f", player.getBalance()));
        if (difference > 0) {
            view.showMessage(String.format("Total profit: $%.2f", difference));
        } else if (difference < 0) {
            view.showMessage(String.format("Total loss: $%.2f", Math.abs(difference)));
        } else {
            view.showMessage("No profit or loss.");
        }
        view.showMessage("Thank you for playing.");
    }

    public boolean isRoundActive() {
        return roundActive;
    }

    private boolean allPlayerHandsBust() {
        for (Hand hand : player.getHands()) {
            if (!hand.isBust()) {
                return false;
            }
        }
        return true;
    }

    private void settleHand(Result result, boolean splitHand) {
        double bet = player.getBetAmount();
        switch (result) {
            case BLACKJACK:
                player.adjustBalance(bet * 2.5);
                break;
            case PLAYER_WIN:
            case DEALER_BUST:
                player.adjustBalance(bet * 2.0);
                break;
            case PUSH:
                player.adjustBalance(bet);
                break;
            case DEALER_WIN:
            case PLAYER_BUST:
            default:
                // The wager was already deducted and is not returned.
                break;
        }
    }
}
