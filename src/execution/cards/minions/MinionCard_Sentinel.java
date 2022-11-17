package execution.cards.minions;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public final class MinionCard_Sentinel extends MinionCard {
    public MinionCard_Sentinel(final String description, final ArrayList<String> colors,
                               final int mana, final int ownerIdx, final int health,
                               final int attackDamage) {
        super("Sentinel", description, colors,
                mana, ownerIdx, health, attackDamage,
                false, true, false,
                false, false,
                false, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ErrorType useAbility(final Game game, final Card attackedCard) {
        // This should never be reached!
        return ErrorType.CRITICAL_SENTINEL_NO_ABILITY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Card copy() {
        return new MinionCard_Sentinel(this.description, this.colors, this.mana,
                this.ownerIdx, this.health, this.attackDamage);
    }
}
