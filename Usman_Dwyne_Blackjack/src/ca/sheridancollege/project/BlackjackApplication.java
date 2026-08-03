package ca.sheridancollege.project;

/**
 * Program entry point for the console Blackjack game.
 */
public final class BlackjackApplication {
    private BlackjackApplication() {
        // Utility class; prevent construction.
    }

    public static void main(String[] args) {
        Game game = new BlackjackGame("Blackjack");
        game.play();
    }
}
