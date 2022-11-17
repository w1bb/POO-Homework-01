package execution.cards.heros;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

/**
 * This class represents a Lord Royce card.
 */
public final class HeroCard_LordRoyce extends HeroCard {
    /**
     * This constructor creates a new HeroCard_LordRoyce (Lord Royce) card.
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     */
    public HeroCard_LordRoyce(final String description, final ArrayList<String> colors,
                              final int mana, final int ownerIdx) {
        super("Lord Royce", description, colors,
                mana, ownerIdx, HeroCard.DEFAULT_HEALTH,
                true, false);
    }

    /**
     * {@inheritDoc} For HeroCard_LordRoyce, the Sub-Zero ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cards = game.getBoardRow(row);
        MinionCard cardToChange = null;
        for (MinionCard card : cards) {
            if (card != null) {
                if (cardToChange == null) {
                    cardToChange = card;
                } else if (card.getAttackDamage() > cardToChange.getAttackDamage()) {
                    cardToChange = card;
                }
            }
        }
        if (cardToChange == null) {
            // This should never be reached
            return ErrorType.CRITICAL_LORD_ROYCE_FREEZES_NOTHING;
        }
        cardToChange.freeze();
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     * More specifically, an HeroCard_LordRoyce copy will be created.
     */
    @Override
    public Card copy() {
        return new HeroCard_LordRoyce(this.description, this.colors, this.mana, this.ownerIdx);
    }
}
