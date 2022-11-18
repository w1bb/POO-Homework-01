package execution.cards.minions;

import execution.cards.CardType;
import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

/**
 * This class represents a Disciple card.
 */
public final class MinionCard_Disciple extends MinionCard {
    /**
     * This constructor creates a new MinionCard_Disciple (Disciple) card.
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     * @param health the card's original health
     */
    public MinionCard_Disciple(final String description, final ArrayList<String> colors,
                               final int mana, final int ownerIdx, final int health) {
        super("Disciple", description, colors,
                mana, ownerIdx, health, 0,
                false, true, false,
                false, true,
                false, true);
    }

    /**
     * {@inheritDoc} For MinionCard_Disciple, the God's Plan ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final Card attackedCard) {
        if (attackedCard.getCardType() != CardType.MINION) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS;
        }
        // Increase card's health
        ((MinionCard) attackedCard).health += 2;
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     * More specifically, an MinionCard_Disciple copy will be created.
     */
    @Override
    public Card copy() {
        return new MinionCard_Disciple(this.description, this.colors, this.mana,
                this.ownerIdx, this.health);
    }
}
