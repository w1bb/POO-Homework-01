package execution.cards.heros;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

/**
 * This class represents an Empress Thorina card.
 */
public final class HeroCard_EmpressThorina extends HeroCard {
    /**
     * This constructor creates a new HeroCard_EmpressThorina (Empress Thorina) card.
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     */
    public HeroCard_EmpressThorina(final String description, final ArrayList<String> colors,
                                   final int mana, final int ownerIdx) {
        super("Empress Thorina", description, colors,
                mana, ownerIdx, HeroCard.DEFAULT_HEALTH,
                true, false);
    }

    /**
     * {@inheritDoc} For HeroCard_EmpressThorina, the Low Blow ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cards = game.getBoardRow(row);
        // Find the card with most HP
        MinionCard cardToChange = null;
        for (MinionCard card : cards) {
            if (card != null) {
                if (cardToChange == null) {
                    cardToChange = card;
                } else if (card.getHealth() > cardToChange.getHealth()) {
                    cardToChange = card;
                }
            }
        }
        if (cardToChange == null) {
            // This should never be reached
            return ErrorType.CRITICAL_EMPRESS_THORINA_ATTACKS_NULL;
        }
        // Destroy said card
        cardToChange.destroy();
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     * More specifically, an HeroCard_EmpressThorina copy will be created.
     */
    @Override
    public Card copy() {
        return new HeroCard_EmpressThorina(this.description, this.colors,
                this.mana, this.ownerIdx);
    }
}
