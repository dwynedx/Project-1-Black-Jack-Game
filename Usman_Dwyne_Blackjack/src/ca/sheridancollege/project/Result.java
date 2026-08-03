package ca.sheridancollege.project;

/**
 * Possible results for one Blackjack hand.
 */
public enum Result {
    PLAYER_WIN("Player wins"),
    DEALER_WIN("Dealer wins"),
    PUSH("Push - bet returned"),
    PLAYER_BUST("Player busts"),
    DEALER_BUST("Dealer busts - player wins"),
    BLACKJACK("Blackjack - player wins 3:2");

    private final String message;

    Result(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
