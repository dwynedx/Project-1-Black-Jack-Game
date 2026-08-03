/**
 * SYST 17796 project starter code, extended for the Blackjack project.
 * @author Dancye, 2018.
 * Modified by: Usman and Dwyne, 2026.
 */
package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.Collections;

/*
 * Reusable collection for a deck, hand, or another group of cards.
 * The internal list is encapsulated and is never returned directly.
 */
public class GroupOfCards {
    private final ArrayList<Card> cards;
    private int size;

    /*
     * givenSize is the maximum number of cards allowed in the group
     */
    public GroupOfCards(int givenSize) {
        if (givenSize <= 0) {
            throw new IllegalArgumentException("Group size must be positive.");
        }
        this.size = givenSize;
        this.cards = new ArrayList<Card>(givenSize);
    }

    /*
     * Returns a defensive copy so callers cannot modify the internal list.
     * Returns copy of the cards in this group
     */
    public ArrayList<Card> showCards() {
        return new ArrayList<Card>(cards);
    }

    /*
     * Randomizes the order of the cards.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /*
     * Adds a card while respecting the maximum size.
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null.");
        }
        if (cards.size() >= size) {
            throw new IllegalStateException("This card group is full.");
        }
        cards.add(card);
    }

    /*
     * Removes and returns a card at the given index.
     */
    public Card removeCard(int index) {
        return cards.remove(index);
    }

    /*
     * Removes and returns the last card.
     */
    protected Card removeLastCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("There are no cards to remove.");
        }
        return cards.remove(cards.size() - 1);
    }

    /*
     * Gets a card without exposing the collection itself.
     */
    protected Card getCard(int index) {
        return cards.get(index);
    }

    /*
     * Removes all cards from the group.
     */
    protected void clearCards() {
        cards.clear();
    }

    public int getCardCount() {
        return cards.size();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int givenSize) {
        if (givenSize < cards.size()) {
            throw new IllegalArgumentException("New size cannot be smaller than the current card count.");
        }
        size = givenSize;
    }
}
