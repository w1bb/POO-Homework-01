package execution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import execution.cards.Card;
import execution.cards.CardType;
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

    /**
     * This method resets all player's properties to default / given values.
     * @param playerDeckId the new deck id
     * @param shuffleSeed the new shuffle seed
     * @param heroCard the newly chosen hero card
     */
    public void reset(final int playerDeckId, final int shuffleSeed, final HeroCard heroCard) {
        this.currentDeck = decks.get(playerDeckId);
        this.currentDeck.shuffle(shuffleSeed);
        this.currentHand = new ArrayList<>();
        this.currentHeroCard = heroCard;
        this.currentMana = 0;
    }

    /**
     * Draw a card from the current, shuffled deck and place it into the player's hand.
     */
    public void drawCard() {
        Card drawnCard = currentDeck.drawCard();
        if (drawnCard != null) {
            drawnCard.setOwnerIdx(this.playerIdx);
            this.currentHand.add(drawnCard);
        }
    }

    /**
     * Remove a given card from the hand.
     * @param handIdx the index of the card to be dropped
     */
    public void dropCardFromHand(final int handIdx) {
        currentHand.remove(handIdx);
    }

    /**
     * Getter for any card in the current hand
     * @param handIdx the index of the card to be removed
     * @return the selected card
     */
    public Card getCardFromHand(final int handIdx) {
        return currentHand.get(handIdx);
    }

    /**
     * This method places a card on the board.
     * @param handIdx the index of the card to be placed
     * @param game the current game
     * @return based on the success / failure of the action, return an instance of ErrorType
     */
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
                e = game.pushOnBoardRow(minionCard, Game.BOARD_ROWS / 2 - 1);
            } else {
                e = game.pushOnBoardRow(minionCard, Game.BOARD_ROWS / 2);
            }
        } else if (minionCard.getAllowPlacementOnBackRow()) {
            if (game.isPlayersRow(this.playerIdx, 0)) {
                e = game.pushOnBoardRow(minionCard, 0);
            } else {
                e = game.pushOnBoardRow(minionCard, Game.BOARD_ROWS - 1);
            }
        }
        // If the card has been placed, drop it from the hand and decrease mana left
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

    /**
     * This method converts the current hand into a printable ObjectNode format.
     *
     * @return the converted value
     */
    public ArrayNode currentHandToArrayNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode cardsInside = objectMapper.createArrayNode();
        for (Card card : currentHand) {
            cardsInside.add(card.toObjectNode());
        }
        return cardsInside;
    }

    /**
     * This method converts the current hand's environment cards into a printable
     * ObjectNode format.
     *
     * @return the converted value
     */
    public ArrayNode currentHandEnvironmentToArrayNode() {
        ObjectMapper objectMapper = new ObjectMapper();
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

    /**
     * Increases the amount of wins of the current player.
     */
    public void addVictory() {
        this.wins++;
    }
}
