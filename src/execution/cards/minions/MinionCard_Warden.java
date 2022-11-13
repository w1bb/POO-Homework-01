package execution.cards.minions;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public class MinionCard_Warden extends MinionCard {
    public MinionCard_Warden(String description, ArrayList<String> colors,
                             int mana, int ownerIdx, int health, int attackDamage) {
        super("Warden", description, colors,
                mana, ownerIdx, health, attackDamage,
                true, true, false,
                false, false,
                true, false);
    }

    @Override
    protected ErrorType useAbility(Game game, Card card) {
        // This should never be reached!
        return ErrorType.CRITICAL_WARDEN_NO_ABILITY;
    }
}
