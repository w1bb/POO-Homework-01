package execution.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Deck {
    private final int deckIdx;
    private final int ownerIdx;
    private final ArrayList<Card> orderedCards;
    private ArrayList<Card> shuffledCards;
    private int currentIndex;

    public Deck(final int deckIdx, final int ownerIdx, final ArrayList<Card> orderedCards) {
        this.deckIdx = deckIdx;
        this.ownerIdx = ownerIdx;
        this.orderedCards = orderedCards;
        this.shuffledCards = null;
        this.currentIndex = -1;
    }

    /**
     * This method creates a shallow-copy of the deck and shuffles them using Collection.shuffle()
     * with a given Random seed.
     *
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
     *
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
     *
     * @return the converted value
     */
    public ArrayNode toArrayNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode cardsInside = objectMapper.createArrayNode();
        for (int i = currentIndex; i < shuffledCards.size(); ++i) {
            cardsInside.add(shuffledCards.get(i).toObjectNode());
        }
        return cardsInside;
    }
}
