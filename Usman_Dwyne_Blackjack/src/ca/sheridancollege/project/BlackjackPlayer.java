package ca.sheridancollege.project;

import java.util.HashSet;
import java.util.Set;

/**
 * Blackjack participant with a balance, current wager, and up to two hands.
 */
public class BlackjackPlayer extends Player {
    private double balance;
    private double betAmount;
    private final Set<Integer> standingHands;

    public BlackjackPlayer(String name, String playerID, double balance) {
        super(name, playerID);
        if (balance < 0) {
            throw new IllegalArgumentException("Starting balance cannot be negative.");
        }
        this.balance = balance;
        this.standingHands = new HashSet<Integer>();
    }

    /**
     * Deducts the wager at the start of a round.
     *
     * @param amount requested wager
     * @return true when the wager was accepted
     */
    public boolean placeBet(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        betAmount = amount;
        balance -= amount;
        return true;
    }

    /**
     * Deducts an equal second wager when a hand is split.
     *
     * @return true if the player has enough balance
     */
    public boolean placeSplitBet() {
        if (betAmount <= 0 || betAmount > balance) {
            return false;
        }
        balance -= betAmount;
        return true;
    }

    public void hit(Deck deck, int handIndex) {
        receiveCard(deck.dealCard(), handIndex);
    }

    public void stand(int handIndex) {
        standingHands.add(handIndex);
    }

    public boolean hasStood(int handIndex) {
        return standingHands.contains(handIndex);
    }

    /*
     * Splits the first pair into two hands and deals one new card to each hand.
     * Only one split is allowed in this project scope.
     */
    public boolean split(Deck deck, int handIndex) {
        if (!canSplit(handIndex) || !placeSplitBet()) {
            return false;
        }

        Hand originalHand = getHand(handIndex);
        BlackjackCard secondCard = originalHand.splitOffSecondCard();
        Hand secondHand = new Hand();
        secondHand.addCard(secondCard);
        addHand(secondHand);

        originalHand.addCard(deck.dealCard());
        secondHand.addCard(deck.dealCard());
        return true;
    }

    public boolean canSplit(int handIndex) {
        return getHandCount() == 1
                && getHand(handIndex).isPair()
                && balance >= betAmount;
    }

    public void adjustBalance(double amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public void prepareForRound() {
        clearHands();
        standingHands.clear();
        betAmount = 0;
    }

    @Override
    public void play(Deck deck) {
        while (getHandValue(0) < 17) {
            hit(deck, 0);
        }
        stand(0);
    }
}
