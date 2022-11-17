package execution.cards.heros;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

/**
 * This class represents a King Mudface card.
 */
public final class HeroCard_KingMudface extends HeroCard {
    /**
     * This constructor creates a new HeroCard_KingMudface (King Mudface) card.
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     */
    public HeroCard_KingMudface(final String description, final ArrayList<String> colors,
                                final int mana, final int ownerIdx) {
        super("King Mudface", description, colors,
                mana, ownerIdx, HeroCard.DEFAULT_HEALTH,
                false, true);
    }

    /**
     * {@inheritDoc} For HeroCard_KingMudface, the Earth Born ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cards = game.getBoardRow(row);
        // Increase HP for each card in a row
        for (MinionCard card : cards) {
            if (card != null) {
                card.setHealth(card.getHealth() + 1);
            }
        }
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     * More specifically, an HeroCard_KingMudface copy will be created.
     */
    @Override
    public Card copy() {
        return new HeroCard_KingMudface(this.description, this.colors,
                this.mana, this.ownerIdx);
    }
}
