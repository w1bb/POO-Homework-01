package execution.cards.minions;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public final class MinionCard_Berserker extends MinionCard {
    public MinionCard_Berserker(final String description, final ArrayList<String> colors,
                                final int mana, final int ownerIdx, final int health,
                                final int attackDamage) {
        super("Berserker", description, colors,
                mana, ownerIdx, health, attackDamage,
                false, true, false,
                false, false,
                false, true);
    }

    @Override
    protected ErrorType useAbility(final Game game, final Card attackedCard) {
        // This should never be reached!
        return ErrorType.CRITICAL_BERSERKER_NO_ABILITY;
    }

    @Override
    public Card copy() {
        return new MinionCard_Berserker(this.description, this.colors, this.mana,
                this.ownerIdx, this.health, this.attackDamage);
    }
}
