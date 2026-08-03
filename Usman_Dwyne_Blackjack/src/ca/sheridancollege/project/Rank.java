package ca.sheridancollege.project;

/**
 * Valid card ranks and their basic Blackjack values.
 * An ace begins at 1 and is promoted to 11 by Hand when safe.
 */
public enum Rank {
    ACE("Ace", 1),
    TWO("Two", 2),
    THREE("Three", 3),
    FOUR("Four", 4),
    FIVE("Five", 5),
    SIX("Six", 6),
    SEVEN("Seven", 7),
    EIGHT("Eight", 8),
    NINE("Nine", 9),
    TEN("Ten", 10),
    JACK("Jack", 10),
    QUEEN("Queen", 10),
    KING("King", 10);

    private final String displayName;
    private final int value;

    Rank(String displayName, int value) {
        this.displayName = displayName;
        this.value = value;
    }

    /**
     * @return the rank's base Blackjack value
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
