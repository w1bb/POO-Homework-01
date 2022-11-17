package execution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import execution.cards.Card;
import execution.cards.Deck;
import execution.cards.heros.HeroCard;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public final class Player {
    private final int playerIdx;
    private final ArrayList<Deck> decks;
    private Deck currentDeck;
    private HeroCard currentHeroCard;
    private ArrayList<Card> currentHand;
    private int currentMana;
    private int wins;

    public Player(final int playerIdx, final ArrayList<Deck> decks) {
        this.playerIdx = playerIdx;
        this.decks = decks;
        this.wins = 0;
    }

    public void reset(final int playerDeckId, final int shuffleSeed, final HeroCard heroCard) {
        this.currentDeck = decks.get(playerDeckId);
        this.currentDeck.shuffle(shuffleSeed);
        this.currentHand = new ArrayList<>();
        this.currentHeroCard = heroCard;
        this.currentMana = 0;
    }

    public void drawCard() {
        Card drawnCard = currentDeck.drawCard();
        if (drawnCard != null) {
            drawnCard.setOwnerIdx(this.playerIdx);
            this.currentHand.add(drawnCard);
        }
    }

    public void dropCardFromHand(final int handIdx) {
        currentHand.remove(handIdx); // ?
    }

    public Card getCardFromHand(final int handIdx) {
        return currentHand.get(handIdx);
    }

    public ErrorType placeCardOnBoard(final int handIdx, final Game game) {
        Card card = getCardFromHand(handIdx);
        if (card.getCardType() == CardType.ENVIRONMENT) {
            return ErrorType.ERROR_PLACE_ENVIRONMENT_ON_BOARD;
        }
        if (this.currentMana < card.getMana()) {
            return ErrorType.ERROR_INSUFFICIENT_MANA_TO_PLACE;
        }
        MinionCard minionCard = (MinionCard) card;
        ErrorType e = ErrorType.NO_ERROR;
        if (minionCard.getAllowPlacementOnFrontRow()) {
            if (game.isPlayersRow(this.playerIdx, 1)) {
                e = game.pushOnBoardRow(minionCard, 1);
            } else {
                e = game.pushOnBoardRow(minionCard, 2);
            }
        } else if (minionCard.getAllowPlacementOnBackRow()) {
            if (game.isPlayersRow(this.playerIdx, 0)) {
                e = game.pushOnBoardRow(minionCard, 0);
            } else {
                e = game.pushOnBoardRow(minionCard, 3);
            }
        }
        // ? Is this so?
        if (e == ErrorType.NO_ERROR) {
            this.currentMana -= card.getMana();
            dropCardFromHand(handIdx);
        }
        return e;
    }

    public int getMana() {
        return this.currentMana;
    }

    public void setMana(final int mana) {
        this.currentMana = mana;
    }

    public HeroCard getHeroCard() {
        return this.currentHeroCard;
    }

    public ArrayNode currentHandToArrayNode(final ObjectMapper objectMapper) {
        ArrayNode cardsInside = objectMapper.createArrayNode();
        for (Card card : currentHand) {
            cardsInside.add(card.toObjectNode());
        }
        return cardsInside;
    }

    public ArrayNode currentHandEnvironmentToArrayNode(final ObjectMapper objectMapper) {
        ArrayNode cardsInside = objectMapper.createArrayNode();
        for (Card card : currentHand) {
            if (card.getCardType() == CardType.ENVIRONMENT) {
                cardsInside.add(card.toObjectNode());
            }
        }
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
