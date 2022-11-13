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

    public ErrorType placeCardOnBoard(int handIdx, Game game) {
        Card card = getCardFromHand(handIdx);
        if (card.getCardType() == 2)
            return ErrorType.ERROR_PLACE_ENVIRONMENT_ON_BOARD;
        if (this.currentMana < card.getMana())
            return ErrorType.ERROR_INSUFFICIENT_MANA_TO_PLACE;
        MinionCard minionCard = (MinionCard)card;
        ErrorType e = ErrorType.NO_ERROR;
        if (minionCard.getAllowPlacementOnFrontRow()) {
            if (game.isPlayersRow(this.playerIdx, 1))
                e = game.pushOnBoardRow(minionCard, 1);
            else
                e = game.pushOnBoardRow(minionCard, 2);
        } else if (minionCard.getAllowPlacementOnBackRow()) {
            if (game.isPlayersRow(this.playerIdx, 0))
                e = game.pushOnBoardRow(minionCard, 0);
            else
                e = game.pushOnBoardRow(minionCard, 3);
        }
        // ? Is this so?
        if (e == ErrorType.NO_ERROR)
            dropCardFromHand(handIdx);
        return e;
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

    public void addVictory() {
        this.wins++;
    }
}
