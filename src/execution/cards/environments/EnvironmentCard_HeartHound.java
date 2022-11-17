package execution.cards.environments;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

/**
 * This class represents a Heart Hound card.
 */
public final class EnvironmentCard_HeartHound extends EnvironmentCard {
    /**
     * This constructor creates a new EnvironmentCard_HeartHound (Heart Hound) card.
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     */
    public EnvironmentCard_HeartHound(final String description, final ArrayList<String> colors,
                                      final int mana, final int ownerIdx) {
        super("Heart Hound", description, colors,
                mana, ownerIdx,
                true, false);
    }

    /**
     * {@inheritDoc} For EnvironmentCard_HeartHound, the Heart Hound ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cardsEnemy = game.getBoardRow(row);
        // Find the card with most HP
        MinionCard cardToMove = null;
        for (MinionCard card : cardsEnemy) {
            if (card != null) {
                if (cardToMove == null) {
                    cardToMove = card;
                } else if (card.getHealth() > cardToMove.getHealth()) {
                    cardToMove = card;
                }
            }
        }
        if (cardToMove == null) {
            // This should never be reached!
            return ErrorType.CRITICAL_HEART_HOUND_ABILITY_NO_CARD_TO_MOVE;
        }
        // Move the card on the other side of the board
        cardToMove.setOwnerIdx(Game.BOARD_COLUMNS - cardToMove.getOwnerIdx() - 1);
        return game.pushOnBoardRow(cardToMove, Game.BOARD_COLUMNS - row - 1);
    }

    /**
     * {@inheritDoc}
     * More specifically, an EnvironmentCard_HeartHound copy will be created.
     */
    @Override
    public Card copy() {
        return new EnvironmentCard_HeartHound(this.description, this.colors,
                this.mana, this.ownerIdx);
    }
}
