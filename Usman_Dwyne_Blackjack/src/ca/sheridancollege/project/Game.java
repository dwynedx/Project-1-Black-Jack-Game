/**
 * SYST 17796 project starter code, extended for the Blackjack project.
 * @author: dancye, 2018.
 * Modified by: Usman and Dwyne, 2026.
 */
package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 * General base class for a game with a name and participants.
 */
public abstract class Game {
    private final String gameName;
    private ArrayList<Player> players;

    public Game(String givenName) {
        if (givenName == null || givenName.trim().isEmpty()) {
            throw new IllegalArgumentException("Game name is required.");
        }
        gameName = givenName.trim();
        players = new ArrayList<Player>();
    }

    public String getGameName() {
        return gameName;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<Player>(players);
    }

    public void setPlayers(ArrayList<Player> players) {
        if (players == null) {
            throw new IllegalArgumentException("Player list cannot be null.");
        }
        this.players = new ArrayList<Player>(players);
    }

    public abstract void play();

    public abstract void declareWinner();
}
