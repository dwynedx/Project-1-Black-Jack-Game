package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Cards currently owned by the participant for one Blackjack hand.
 */
public class Hand extends GroupOfCards {
    private static final int MAX_HAND_SIZE = 12;

    public Hand() {
        super(MAX_HAND_SIZE);
    }

    @Override
    public void addCard(Card card) {
        if (!(card instanceof BlackjackCard)) {
            throw new IllegalArgumentException("A Blackjack hand only accepts BlackjackCard objects.");
        }
        super.addCard(card);
    }

    /*
     * Calculates the best legal value. Aces count as 11 when possible,
     * otherwise they count as 1.
     * Returns best hand value
     */
    public int calculateValue() {
        int total = 0;
        int aceCount = 0;

        for (Card card : showCards()) {
            total += card.getValue();
            if (((BlackjackCard) card).getRank() == Rank.ACE) {
                aceCount++;
            }
        }

        while (aceCount > 0 && total + 10 <= 21) {
            total += 10;
            aceCount--;
        }
        return total;
    }

    public boolean isBust() {
        return calculateValue() > 21;
    }

    public boolean isBlackjack() {
        return getCardCount() == 2 && calculateValue() == 21;
    }

    public boolean isPair() {
        if (getCardCount() != 2) {
            return false;
        }
        BlackjackCard first = (BlackjackCard) getCard(0);
        BlackjackCard second = (BlackjackCard) getCard(1);
        return first.getRank() == second.getRank();
    }

    /*
     * Removes the second card when a pair is split.
     * Return second card from the original hand
     */
    public BlackjackCard splitOffSecondCard() {
        if (!isPair()) {
            throw new IllegalStateException("Only a pair can be split.");
        }
        return (BlackjackCard) removeCard(1);
    }

    public void reset() {
        clearCards();
    }

    public List<BlackjackCard> getBlackjackCards() {
        ArrayList<BlackjackCard> result = new ArrayList<BlackjackCard>();
        for (Card card : showCards()) {
            result.add((BlackjackCard) card);
        }
        return result;
    }

    @Override
    public String toString() {
        return getBlackjackCards() + " (value: " + calculateValue() + ")";
    }
}
