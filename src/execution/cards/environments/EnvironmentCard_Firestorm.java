package execution.cards.environments;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

/**
 * This class represents a Firestorm card.
 */
public final class EnvironmentCard_Firestorm extends EnvironmentCard {
    /**
     * This constructor creates a new EnvironmentCard_Firestorm (Firestorm) card.
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     */
    public EnvironmentCard_Firestorm(final String description, final ArrayList<String> colors,
                                     final int mana, final int ownerIdx) {
        super("Firestorm", description, colors,
                mana, ownerIdx,
                true, false);
    }

    /**
     * {@inheritDoc} For EnvironmentCard_Firestorm, the Firestorm ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cards = game.getBoardRow(row);
        // Damage each minion by 1
        for (MinionCard card : cards) {
            if (card != null) {
                card.damage(1);
            }
        }
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     * More specifically, an EnvironmentCard_Firestorm copy will be created.
     */
    @Override
    public Card copy() {
        return new EnvironmentCard_Firestorm(this.description, this.colors,
                this.mana, this.ownerIdx);
    }
}
