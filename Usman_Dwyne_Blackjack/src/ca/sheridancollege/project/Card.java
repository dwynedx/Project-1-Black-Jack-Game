/**
 * SYST 17796 project starter code, extended for the Blackjack project.
 * @author: dancye, 2018.
 * Modified by: Usman and Dwyne, 2026.
 */
package ca.sheridancollege.project;

/**
 * General base type for cards used by a card game.
 * Concrete games provide the card value and display format.
 */
public abstract class Card {

    /**
     * Returns the value of this card for the current game.
     *
     * @return numerical game value
     */
    public abstract int getValue();

    /**
     * Returns a readable description of the card.
     *
     * @return card description
     */
    @Override
    public abstract String toString();
}
