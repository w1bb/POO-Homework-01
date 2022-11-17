package execution.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This class is capable of storing, shuffling and dealing a pack (an array) of cards. Internally,
 * the cards are stored in an ArrayList and shallow-copied in another ArrayList for every shuffle.
 * Dealing the cards will be efficient because elements aren't actually removed from an array, but
 * an index is incremented with each card dealt.
 */
public final class Deck {
    private final ArrayList<Card> orderedCards;
    private ArrayList<Card> shuffledCards;
    private int currentIndex;

    /**
     * This constructor sets up the deck based on an ArrayList of Card.
     * @param orderedCards the cards in their initial order
     */
    public Deck(final ArrayList<Card> orderedCards) {
        this.orderedCards = orderedCards;
        // No shuffled cards yet
        this.shuffledCards = null;
        this.currentIndex = -1;
    }

    /**
     * This method creates a shallow-copy of the deck and shuffles them using Collection.shuffle()
     * with a given Random seed.
     * @param seed the seed used by the Random instance
     */
    public void shuffle(final int seed) {
        // Create a shallow-copy of the cards
        shuffledCards = new ArrayList<>();
        shuffledCards.addAll(orderedCards);
        // Shuffle the cards
        Collections.shuffle(shuffledCards, new Random(seed));
        // Reposition the first card to the beginning
        currentIndex = 0;
    }

    /**
     * This method creates a deep-copy of the card that should be drawn and returns it.
     * @return the current card (or <code>null</code> if there are no more cards in the deck)
     */
    public Card drawCard() {
        // Check if there are enough cards in the deck
        if (currentIndex >= shuffledCards.size()) {
            return null;
        }
        // Create & return a deep-copy and move to the next card
        Card card = shuffledCards.get(currentIndex).copy();
        currentIndex++;
        return card;
    }

    /**
     * This method converts the given format into a printable ArrayNode format.
     * @return the converted value
     */
    public ArrayNode toArrayNode() {
        // Create the ArrayNode that will be returned
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode cardsInside = objectMapper.createArrayNode();
        // Add each card individually
        for (int i = currentIndex; i < shuffledCards.size(); ++i) {
            cardsInside.add(shuffledCards.get(i).toObjectNode());
        }
        // Return the constructed ArrayNode
        return cardsInside;
    }
}
