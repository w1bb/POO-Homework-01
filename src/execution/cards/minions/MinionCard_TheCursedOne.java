package execution.cards.minions;

import execution.CardType;
import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public final class MinionCard_TheCursedOne extends MinionCard {
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
        // Swap using xor
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
