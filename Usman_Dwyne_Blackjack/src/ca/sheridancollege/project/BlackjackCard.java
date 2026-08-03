package ca.sheridancollege.project;

import java.util.Objects;


public final class BlackjackCard extends Card {
    private final Suit suit;
    private final Rank rank;

    public BlackjackCard(Suit suit, Rank rank) {
        if (suit == null || rank == null) {
            throw new IllegalArgumentException("Suit and rank are required.");
        }
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BlackjackCard)) {
            return false;
        }
        BlackjackCard card = (BlackjackCard) other;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }
}
