package execution.cards.minions;

import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public class MinionCard_Goliath extends MinionCard {
    public MinionCard_Goliath(String description, ArrayList<String> colors,
                              int mana, int ownerIdx, int health, int attackDamage) {
        super("Goliath", description, colors,
                mana, ownerIdx, health, attackDamage,
                true, true, false,
                false, false,
                true, false);
    }

    @Override
    protected Boolean useAbility(Game game, Card card) {
        // This should never be reached!
        System.out.println("CRITICAL: Goliath has NO ability implemented.");
        return false;
    }
}
