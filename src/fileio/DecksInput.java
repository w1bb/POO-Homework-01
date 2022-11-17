package fileio;

import execution.cards.Card;
import execution.cards.Deck;

import java.util.ArrayList;

public final class DecksInput {
    private int nrCardsInDeck;
    private int nrDecks;
    private ArrayList<ArrayList<CardInput>> decks;

    public DecksInput() {
    }

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public int getNrDecks() {
        return nrDecks;
    }

    public void setNrDecks(final int nrDecks) {
        this.nrDecks = nrDecks;
    }

    public ArrayList<ArrayList<CardInput>> getDecks() {
        return decks;
    }

    public void setDecks(final ArrayList<ArrayList<CardInput>> decks) {
        this.decks = decks;
    }

    public ArrayList<Deck> toArrayOfDeck(final int ownerIdx) {
        ArrayList<Deck> decksReturn = new ArrayList<>();
        for (int deckIdx = 0; deckIdx < decks.size(); ++deckIdx) {
            ArrayList<Card> cards = new ArrayList<>();
            for (CardInput cardInput : decks.get(deckIdx)) {
                cards.add(cardInput.toCard(ownerIdx));
            }
            decksReturn.add(new Deck(deckIdx, ownerIdx, cards));
        }
        return decksReturn;
    }

    @Override
    public String toString() {
        return "InfoInput{"
                + "nr_cards_in_deck="
                + nrCardsInDeck
                +  ", nr_decks="
                + nrDecks
                + ", decks="
                + decks
                + '}';
    }
}
