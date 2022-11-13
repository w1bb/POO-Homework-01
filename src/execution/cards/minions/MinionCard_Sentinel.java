package execution.cards.minions;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public class MinionCard_Sentinel extends MinionCard {
    public MinionCard_Sentinel(String description, ArrayList<String> colors,
                               int mana, int ownerIdx, int health, int attackDamage) {
        super("Sentinel", description, colors,
                mana, ownerIdx, health, attackDamage,
                false, true, false,
                false, false,
                false, true);
    }

    @Override
    protected ErrorType useAbility(Game game, Card card) {
        // This should never be reached!
        return ErrorType.CRITICAL_SENTINEL_NO_ABILITY;
    }

    @Override
    public Card copy() {
        return new MinionCard_Sentinel(this.description, this.colors, this.mana,
                this.ownerIdx, this.health, this.attackDamage);
    }
}
