package execution.cards.minions;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public final class MinionCard_Goliath extends MinionCard {
    public MinionCard_Goliath(final String description, final ArrayList<String> colors,
                              final int mana, final int ownerIdx, final int health,
                              final int attackDamage) {
        super("Goliath", description, colors,
                mana, ownerIdx, health, attackDamage,
                true, true, false,
                false, false,
                true, false);
    }

    @Override
    protected ErrorType useAbility(final Game game, final Card attackedCard) {
        // This should never be reached!
        return ErrorType.CRITICAL_GOLIATH_NO_ABILITY;
    }

    @Override
    public Card copy() {
        return new MinionCard_Goliath(this.description, this.colors, this.mana,
                this.ownerIdx, this.health, this.attackDamage);
    }
}
