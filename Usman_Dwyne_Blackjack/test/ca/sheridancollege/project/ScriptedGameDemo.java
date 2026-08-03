package ca.sheridancollege.project;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Repeatable end-to-end demonstration of betting, splitting, hitting,
 * standing, dealer play, hand comparison, settlement, and cashing out.
 */
public final class ScriptedGameDemo {
    private ScriptedGameDemo() {
    }

    public static void main(String[] args) {
        Deck deck = Deck.withTopCards(Arrays.asList(
                new BlackjackCard(Suit.HEARTS, Rank.EIGHT),   // player card 1
                new BlackjackCard(Suit.CLUBS, Rank.NINE),    // dealer card 1
                new BlackjackCard(Suit.SPADES, Rank.EIGHT),  // player card 2
                new BlackjackCard(Suit.DIAMONDS, Rank.SEVEN),// dealer card 2 = 16
                new BlackjackCard(Suit.CLUBS, Rank.THREE),   // split hand 1 = 11
                new BlackjackCard(Suit.HEARTS, Rank.KING),   // split hand 2 = 18
                new BlackjackCard(Suit.SPADES, Rank.TEN),    // player hits to 21
                new BlackjackCard(Suit.DIAMONDS, Rank.FIVE)  // dealer hits to 21
        ));

        String scriptedInput = String.join(System.lineSeparator(),
                "Demo Player",
                "10",
                "3", // split
                "1", // hit first hand to 21
                "2", // stand on second hand at 18
                "n"  // cash out after the round
        ) + System.lineSeparator();

        Scanner scanner = new Scanner(new ByteArrayInputStream(
                scriptedInput.getBytes(StandardCharsets.UTF_8)));
        ConsoleBlackjackView view = new ConsoleBlackjackView(scanner, System.out);
        BlackjackGame game = new BlackjackGame("Blackjack - Scripted Playability Demo", deck, view);
        game.play();
    }
}
