package ca.sheridancollege.project;

/**
 * Dealer participant that follows the fixed rule of hitting below 17.
 */
public class Dealer extends Player {
    private boolean hiddenCardRevealed;

    public Dealer(String name, String playerID) {
        super(name, playerID);
    }

    public void revealHiddenCard() {
        hiddenCardRevealed = true;
    }

    public void hideSecondCard() {
        hiddenCardRevealed = false;
    }

    public boolean isHiddenCardRevealed() {
        return hiddenCardRevealed;
    }

    public boolean shouldHit() {
        return getHandValue(0) < 17;
    }

    @Override
    public void play(Deck deck) {
        revealHiddenCard();
        while (shouldHit()) {
            receiveCard(deck.dealCard(), 0);
        }
    }
}
