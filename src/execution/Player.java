package execution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.cards.Card;
import execution.cards.Deck;
import execution.cards.heros.HeroCard;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public class Player {
    private int playerIdx;
    private ArrayList<Deck> decks;
    private Deck currentDeck;
    private HeroCard currentHeroCard;
    private ArrayList<Card> currentHand;
    private int currentMana;
    private int wins;

    public Player(int playerIdx, ArrayList<Deck> decks) {
        this.playerIdx = playerIdx;
        this.decks = decks;
        this.wins = 0;
    }

    public void reset(int playerDeckId, int shuffleSeed, HeroCard heroCard) {
        this.currentDeck = decks.get(playerDeckId);
        this.currentDeck.shuffle(shuffleSeed);
        this.currentHand = new ArrayList<>();
        this.currentHeroCard = heroCard;
        this.currentMana = 0;
    }

    public void drawCard() {
        Card drawnCard = currentDeck.drawCard();
        drawnCard.setOwnerIdx(this.playerIdx);
        this.currentHand.add(drawnCard);
    }

    public Card dropCardFromHand(int handIdx) {
        Card card = currentHand.get(handIdx);
        currentHand.remove(handIdx); // ?
        return card;
    }

    public Card getCardFromHand(int handIdx) {
        return currentHand.get(handIdx);
    }

    public String placeCardOnBoard(int handIdx, Game game) {
        Card card = getCardFromHand(handIdx);
        if (card.getCardType() == 2)
            return "Cannot place environment card on table.";
        if (this.currentMana < card.getMana())
            return "Not enough mana to place card on table.";
        MinionCard minionCard = (MinionCard)card;
        if (minionCard.getAllowPlacementOnFrontRow()) {
            if (game.isPlayersRow(this.playerIdx, 1))

        }
        return null;
    }

    public int getMana() {
        return this.currentMana;
    }

    public void setMana(int mana) {
        this.currentMana = mana;
    }

    public HeroCard getHeroCard() {
        return this.currentHeroCard;
    }

    public ArrayNode currentHandToArrayNode(ObjectMapper objectMapper) {
        ArrayNode cardsInside = objectMapper.createArrayNode();
        for (Card card : currentHand)
            cardsInside.add(card.toObjectNode(objectMapper));
        return cardsInside;
    }

    public ArrayNode currentHandEnvironmentToArrayNode(ObjectMapper objectMapper) {
        ArrayNode cardsInside = objectMapper.createArrayNode();
        for (Card card : currentHand)
            if (card.getCardType() == 2)
                cardsInside.add(card.toObjectNode(objectMapper));
        return cardsInside;
    }

    public Deck getCurrentDeck() {
        return currentDeck;
    }

    public int getWins() {
        return this.wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}
