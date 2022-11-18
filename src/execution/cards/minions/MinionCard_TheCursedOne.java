package execution.cards.minions;

import execution.cards.CardType;
import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

/**
 * This class represents The Cursed One card.
 */
public final class MinionCard_TheCursedOne extends MinionCard {
    /**
     * This constructor creates a new MinionCard_TheCursedOne (The Cursed One) card.
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     * @param health the card's original health
     */
    public MinionCard_TheCursedOne(final String description, final ArrayList<String> colors,
                                   final int mana, final int ownerIdx, final int health) {
        super("The Cursed One", description, colors,
                mana, ownerIdx, health, 0,
                false, true, false,
                true, false,
                false, true);
    }

    /**
     * {@inheritDoc} For MinionCard_TheCursedOne, the Shapeshift ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final Card attackedCard) {
        if (attackedCard.getCardType() != CardType.MINION) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS;
        }
        // Swap attacked health with attacked damage using xor (a ^= b; b ^= a; a ^= b)
        ((MinionCard) attackedCard).attackDamage ^= ((MinionCard) attackedCard).health;
        ((MinionCard) attackedCard).health ^= ((MinionCard) attackedCard).attackDamage;
        ((MinionCard) attackedCard).attackDamage ^= ((MinionCard) attackedCard).health;
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     * More specifically, an MinionCard_TheCursedOne copy will be created.
     */
    @Override
    public Card copy() {
        return new MinionCard_TheCursedOne(this.description, this.colors, this.mana,
                this.ownerIdx, this.health);
    }
}
