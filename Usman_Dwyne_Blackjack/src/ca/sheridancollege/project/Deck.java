package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.List;

/**
 * A standard 52-card Blackjack deck.
 */
public class Deck extends GroupOfCards {
    public static final int STANDARD_DECK_SIZE = 52;

    public Deck() {
        super(STANDARD_DECK_SIZE);
        initializeDeck();
        shuffle();
    }

    /**
     * Creates all 52 unique combinations of suit and rank.
     */
    public final void initializeDeck() {
        clearCards();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                addCard(new BlackjackCard(suit, rank));
            }
        }
    }

    /**
     * Deals one card from the top of the deck.
     *
     * @return next Blackjack card
     */
    public BlackjackCard dealCard() {
        return (BlackjackCard) removeLastCard();
    }

    /**
     * Rebuilds and shuffles the deck.
     */
    public void reset() {
        initializeDeck();
        shuffle();
    }

    public int cardsRemaining() {
        return getCardCount();
    }

    /*
     * Creates a valid 52-card deck with selected cards placed on top.
     * This supports repeatable demonstrations and tests without changing game logic.
     */
    public static Deck withTopCards(List<BlackjackCard> topCards) {
        if (topCards == null) {
            throw new IllegalArgumentException("Top cards are required.");
        }

        Deck deck = new Deck();
        ArrayList<Card> standardCards = deck.showCards();
        deck.clearCards();

        for (BlackjackCard topCard : topCards) {
            if (!standardCards.remove(topCard)) {
                throw new IllegalArgumentException("The requested top cards contain a duplicate or invalid card: " + topCard);
            }
        }

        for (Card card : standardCards) {
            deck.addCard(card);
        }

        // The deck deals from the end, so add the requested top cards in reverse.
        for (int index = topCards.size() - 1; index >= 0; index--) {
            deck.addCard(topCards.get(index));
        }
        return deck;
    }
}
