package execution.cards.minions;

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
    protected Boolean useAbility(Game game, Card card) {
        // This should never be reached!
        System.out.println("CRITICAL: Sentinel has NO ability implemented.");
        return false;
    }
}
