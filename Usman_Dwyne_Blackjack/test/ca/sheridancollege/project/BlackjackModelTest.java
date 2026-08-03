package ca.sheridancollege.project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Lightweight regression tests that can run without an external test library.
 * Each test covers normal, invalid, or boundary behaviour from the game rules.
 */
public final class BlackjackModelTest {
    private static int testsPassed;

    private BlackjackModelTest() {
    }

    public static void main(String[] args) {
        testDeckContainsFiftyTwoCards();
        testRiggedDeckDealsInRequestedOrder();
        testAceCanCountAsEleven();
        testMultipleAcesAdjustToAvoidBust();
        testBustBoundary();
        testPairDetection();
        testInvalidBetIsRejected();
        testValidBetChangesBalance();
        testFullBalanceBoundaryBetIsAccepted();
        testConsoleRejectsInvalidInput();
        testSplitCreatesTwoHandsAndEqualBet();
        testDealerHitsBelowSeventeen();

        System.out.println("----------------------------------------");
        System.out.println("All " + testsPassed + " Blackjack model tests passed.");
    }

    private static void testDeckContainsFiftyTwoCards() {
        Deck deck = new Deck();
        assertEquals(52, deck.cardsRemaining(), "A new deck contains 52 cards");
    }

    private static void testRiggedDeckDealsInRequestedOrder() {
        BlackjackCard first = card(Suit.HEARTS, Rank.ACE);
        BlackjackCard second = card(Suit.SPADES, Rank.KING);
        Deck deck = Deck.withTopCards(Arrays.asList(first, second));
        assertEquals(first, deck.dealCard(), "First configured card is dealt first");
        assertEquals(second, deck.dealCard(), "Second configured card is dealt second");
    }

    private static void testAceCanCountAsEleven() {
        Hand hand = hand(card(Suit.HEARTS, Rank.ACE), card(Suit.CLUBS, Rank.NINE));
        assertEquals(20, hand.calculateValue(), "Ace is promoted to 11 when safe");
    }

    private static void testMultipleAcesAdjustToAvoidBust() {
        Hand hand = hand(
                card(Suit.HEARTS, Rank.ACE),
                card(Suit.CLUBS, Rank.ACE),
                card(Suit.SPADES, Rank.NINE));
        assertEquals(21, hand.calculateValue(), "Only one ace remains high when two aces are present");
    }

    private static void testBustBoundary() {
        Hand hand = hand(card(Suit.HEARTS, Rank.KING), card(Suit.CLUBS, Rank.QUEEN));
        assertFalse(hand.isBust(), "A value of exactly 20 is not a bust");
        hand.addCard(card(Suit.SPADES, Rank.TWO));
        assertTrue(hand.isBust(), "A value above 21 is a bust");
    }

    private static void testPairDetection() {
        Hand hand = hand(card(Suit.HEARTS, Rank.EIGHT), card(Suit.SPADES, Rank.EIGHT));
        assertTrue(hand.isPair(), "Two cards with the same rank form a splittable pair");
    }

    private static void testInvalidBetIsRejected() {
        BlackjackPlayer player = new BlackjackPlayer("Test Player", "P1", 100.00);
        assertFalse(player.placeBet(101.00), "A bet larger than the balance is rejected");
        assertEquals(100.00, player.getBalance(), 0.001, "Rejected bet does not change the balance");
    }

    private static void testValidBetChangesBalance() {
        BlackjackPlayer player = new BlackjackPlayer("Test Player", "P1", 100.00);
        assertTrue(player.placeBet(25.00), "A valid bet is accepted");
        assertEquals(75.00, player.getBalance(), 0.001, "Accepted bet is deducted once");
    }


    private static void testFullBalanceBoundaryBetIsAccepted() {
        BlackjackPlayer player = new BlackjackPlayer("Test Player", "P1", 100.00);
        assertTrue(player.placeBet(100.00), "A bet equal to the full available balance is accepted");
        assertEquals(0.00, player.getBalance(), 0.001, "Full-balance bet reaches the zero-dollar boundary");
    }

    private static void testConsoleRejectsInvalidInput() {
        String input = String.join(System.lineSeparator(),
                "not-a-number",
                "0",
                "150",
                "25",
                "9",
                "2",
                "maybe",
                "n") + System.lineSeparator();
        Scanner scanner = new Scanner(new ByteArrayInputStream(
                input.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        ConsoleBlackjackView view = new ConsoleBlackjackView(scanner, new PrintStream(capturedOutput));

        assertEquals(25.00, view.askBet(100.00), 0.001,
                "Console rejects non-numeric, zero, and over-balance bets");
        assertEquals(2, view.askAction(false),
                "Console rejects an unavailable action and accepts stand");
        assertFalse(view.askPlayAgain(),
                "Console rejects an invalid yes/no response and accepts no");
    }

    private static void testSplitCreatesTwoHandsAndEqualBet() {
        BlackjackPlayer player = new BlackjackPlayer("Test Player", "P1", 100.00);
        player.placeBet(10.00);
        player.receiveCard(card(Suit.HEARTS, Rank.EIGHT), 0);
        player.receiveCard(card(Suit.SPADES, Rank.EIGHT), 0);

        Deck deck = Deck.withTopCards(Arrays.asList(
                card(Suit.CLUBS, Rank.THREE),
                card(Suit.DIAMONDS, Rank.KING)));

        assertTrue(player.split(deck, 0), "A pair can be split when the second bet is affordable");
        assertEquals(2, player.getHandCount(), "Split creates a second hand");
        assertEquals(80.00, player.getBalance(), 0.001, "Split deducts an equal second bet");
        assertEquals(11, player.getHandValue(0), "First split hand receives a card");
        assertEquals(18, player.getHandValue(1), "Second split hand receives a card");
    }

    private static void testDealerHitsBelowSeventeen() {
        Dealer dealer = new Dealer("Dealer", "D1");
        dealer.receiveCard(card(Suit.HEARTS, Rank.NINE), 0);
        dealer.receiveCard(card(Suit.CLUBS, Rank.SEVEN), 0);
        Deck deck = Deck.withTopCards(Arrays.asList(card(Suit.SPADES, Rank.FIVE)));
        dealer.play(deck);
        assertEquals(21, dealer.getHandValue(0), "Dealer hits a value of 16");
        assertFalse(dealer.shouldHit(), "Dealer stands after reaching at least 17");
    }

    private static BlackjackCard card(Suit suit, Rank rank) {
        return new BlackjackCard(suit, rank);
    }

    private static Hand hand(BlackjackCard... cards) {
        Hand hand = new Hand();
        for (BlackjackCard card : cards) {
            hand.addCard(card);
        }
        return hand;
    }

    private static void assertTrue(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError("FAILED: " + description);
        }
        pass(description);
    }

    private static void assertFalse(boolean condition, String description) {
        assertTrue(!condition, description);
    }

    private static void assertEquals(Object expected, Object actual, String description) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError("FAILED: " + description
                    + " (expected " + expected + ", actual " + actual + ")");
        }
        pass(description);
    }

    private static void assertEquals(double expected, double actual, double tolerance, String description) {
        if (Math.abs(expected - actual) > tolerance) {
            throw new AssertionError("FAILED: " + description
                    + " (expected " + expected + ", actual " + actual + ")");
        }
        pass(description);
    }

    private static void pass(String description) {
        testsPassed++;
        System.out.println("PASS: " + description);
    }
}
