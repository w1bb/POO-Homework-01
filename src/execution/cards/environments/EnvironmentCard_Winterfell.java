package execution.cards.environments;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

/**
 * This class represents a Winterfell card.
 */
public final class EnvironmentCard_Winterfell extends EnvironmentCard {
    /**
     * This constructor creates a new EnvironmentCard_Winterfell (Winterfell) card.
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     */
    public EnvironmentCard_Winterfell(final String description, final ArrayList<String> colors,
                                      final int mana, final int ownerIdx) {
        super("Winterfell", description, colors,
                mana, ownerIdx,
                true, false);
    }

    /**
     * {@inheritDoc} For EnvironmentCard_Winterfell, the Winterfell ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cards = game.getBoardRow(row);
        // Freeze all cards on a row
        for (MinionCard card : cards) {
            if (card != null) {
                card.freeze();
            }
        }
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     * More specifically, an EnvironmentCard_Winterfell copy will be created.
     */
    @Override
    public Card copy() {
        return new EnvironmentCard_Winterfell(this.description, this.colors,
                this.mana, this.ownerIdx);
    }
}
