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

    public void shuffle(final int seed) {
        shuffledCards = new ArrayList<>();
        shuffledCards.addAll(orderedCards);
        Collections.shuffle(shuffledCards, new Random(seed));
        currentIndex = 0;
    }

    public Card drawCard() {
        if (currentIndex >= shuffledCards.size()) {
            return null;
        }
        Card card = shuffledCards.get(currentIndex).copy();
        currentIndex++;
        return card;
    }

    public ArrayNode toArrayNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode cardsInside = objectMapper.createArrayNode();
        for (int i = currentIndex; i < shuffledCards.size(); ++i) {
            cardsInside.add(shuffledCards.get(i).toObjectNode());
        }
        return cardsInside;
    }
}
