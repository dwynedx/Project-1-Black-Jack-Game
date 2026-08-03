/**
 * SYST 17796 project starter code, extended for the Blackjack project.
 * @author dancye, 2018.
 * Modified by: Usman and Dwyne, 2026.
 */
package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 * General participant in the game. Common hand operations are implemented once
 * and reused by the human Blackjack player and the dealer.
 */
public abstract class Player {
    private String playerID;
    private String name;
    private final ArrayList<Hand> hands;

    public Player(String name, String playerID) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name is required.");
        }
        if (playerID == null || playerID.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID is required.");
        }
        this.name = name.trim();
        this.playerID = playerID.trim();
        this.hands = new ArrayList<Hand>();
        this.hands.add(new Hand());
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String givenID) {
        if (givenID == null || givenID.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID is required.");
        }
        playerID = givenID.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name is required.");
        }
        this.name = name.trim();
    }

    public void receiveCard(BlackjackCard card, int handIndex) {
        getHand(handIndex).addCard(card);
    }

    public int getHandValue(int handIndex) {
        return getHand(handIndex).calculateValue();
    }

    public Hand getHand(int handIndex) {
        return hands.get(handIndex);
    }

    public int getHandCount() {
        return hands.size();
    }

    /**
     * Returns a defensive copy of the hand list.
     */
    public ArrayList<Hand> getHands() {
        return new ArrayList<Hand>(hands);
    }

    protected void addHand(Hand hand) {
        if (hand == null) {
            throw new IllegalArgumentException("Hand cannot be null.");
        }
        hands.add(hand);
    }

    /**
     * Restores the participant to one empty hand for a new round.
     */
    public void clearHands() {
        hands.clear();
        hands.add(new Hand());
    }

    /**
     * Allows a participant implementation to perform an automatic turn.
     * Human choices are normally coordinated by BlackjackGame.
     *
     * @param deck active deck
     */
    public abstract void play(Deck deck);
}
