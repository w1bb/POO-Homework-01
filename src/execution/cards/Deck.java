package execution.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    private int deckIdx;
    private int ownerIdx;
    private ArrayList<Card> orderedCards;
    private ArrayList<Card> shuffledCards;
    private int currentIndex;

    public Deck(int deckIdx, int ownerIdx, ArrayList<Card> orderedCards) {
        this.deckIdx = deckIdx;
        this.ownerIdx = ownerIdx;
        this.orderedCards = orderedCards;
        this.shuffledCards = null;
        this.currentIndex = -1;
    }

    public void shuffle(int seed) {
        shuffledCards = new ArrayList<>();
        shuffledCards.addAll(orderedCards);
        Collections.shuffle(shuffledCards, new Random(seed));
        currentIndex = 0;
    }

    public Card drawCard() {
        if (currentIndex >= shuffledCards.size())
            return null;
        Card card = shuffledCards.get(currentIndex).copy();
        currentIndex++;
        return card;
    }

    public ArrayNode toArrayNode(ObjectMapper objectMapper) {
        ArrayNode cardsInside = objectMapper.createArrayNode();
        for (int i = currentIndex; i < shuffledCards.size(); ++i)
            cardsInside.add(shuffledCards.get(i).toObjectNode(objectMapper));
        return cardsInside;
    }
}
