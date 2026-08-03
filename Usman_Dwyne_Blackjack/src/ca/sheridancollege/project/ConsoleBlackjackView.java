package ca.sheridancollege.project;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Input/output for Blackjack.
 */
public class ConsoleBlackjackView {
    private final Scanner scanner;
    private final PrintStream output;

    public ConsoleBlackjackView(Scanner scanner, PrintStream output) {
        if (scanner == null || output == null) {
            throw new IllegalArgumentException("Scanner and output are required.");
        }
        this.scanner = scanner;
        this.output = output;
    }

    public void showTitle(String gameName) {
        output.println("========================================");
        output.println("              " + gameName);
        output.println("========================================");
        output.println("Get closer to 21 than the dealer without going over.");
        output.println("Number cards use face value, face cards are 10, and aces are 1 or 11.");
        output.println();
    }

    public String askPlayerName() {
        while (true) {
            output.print("Enter player name: ");
            String name = readLine();
            if (!name.isEmpty()) {
                return name;
            }
            output.println("Please enter a name.");
        }
    }

    public double askBet(double availableBalance) {
        while (true) {
            output.printf("Balance: $%.2f. Enter bet amount: $", availableBalance);
            String input = readLine();
            try {
                double bet = Double.parseDouble(input);
                if (bet > 0 && bet <= availableBalance) {
                    return bet;
                }
            } catch (NumberFormatException exception) {
                // The message below handles non-numeric and out-of-range input.
            }
            output.printf("Enter a number greater than 0 and no more than $%.2f.%n", availableBalance);
        }
    }

    /**
     * @return 1 for hit, 2 for stand, or 3 for split
     */
    public int askAction(boolean splitAvailable) {
        while (true) {
            output.print(splitAvailable
                    ? "Choose action [1=Hit, 2=Stand, 3=Split]: "
                    : "Choose action [1=Hit, 2=Stand]: ");
            String input = readLine();
            if ("1".equals(input) || "2".equals(input)
                    || (splitAvailable && "3".equals(input))) {
                return Integer.parseInt(input);
            }
            output.println("That action is not available.");
        }
    }

    public boolean askPlayAgain() {
        while (true) {
            output.print("Play another round? [y/n]: ");
            String input = readLine().toLowerCase();
            if ("y".equals(input) || "yes".equals(input)) {
                return true;
            }
            if ("n".equals(input) || "no".equals(input)) {
                return false;
            }
            output.println("Please enter y or n.");
        }
    }

    public void showDealerHand(Dealer dealer, boolean revealAll) {
        Hand hand = dealer.getHand(0);
        if (!revealAll && hand.getCardCount() >= 2) {
            output.println("Dealer hand: [" + hand.getBlackjackCards().get(0) + ", Hidden card]");
        } else {
            output.println("Dealer hand: " + hand);
        }
    }

    public void showPlayerHand(BlackjackPlayer player, int handIndex) {
        output.println("Player hand " + (handIndex + 1) + ": " + player.getHand(handIndex));
    }

    public void showMessage(String message) {
        output.println(message);
    }

    public void showBlankLine() {
        output.println();
    }

    private String readLine() {
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("No more input is available.");
        }
        return scanner.nextLine().trim();
    }
}
