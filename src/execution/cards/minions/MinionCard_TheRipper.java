package execution.cards.minions;

import execution.CardType;
import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public final class MinionCard_TheRipper extends MinionCard {
    public MinionCard_TheRipper(final String description, final ArrayList<String> colors,
                                final int mana, final int ownerIdx, final int health,
                                final int attackDamage) {
        super("The Ripper", description, colors,
                mana, ownerIdx, health, attackDamage,
                false, true, false,
                true, false,
                true, false);
    }

    /**
     * {@inheritDoc} For MinionCard_TheRipper, the Weak Knees ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final Card attackedCard) {
        final int abilityADExtract = 2;
        // Check if the card is a minion
        if (attackedCard.getCardType() != CardType.MINION) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS;
        }
        // Remove ABILITY_AD_EXTRACT AD from the card
        ((MinionCard) attackedCard).attackDamage =
                Math.max(((MinionCard) attackedCard).attackDamage - abilityADExtract, 0);
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     * More specifically, an MinionCard_TheRipper copy will be created.
     */
    @Override
    public Card copy() {
        return new MinionCard_TheRipper(this.description, this.colors, this.mana,
                this.ownerIdx, this.health, this.attackDamage);
    }
}
