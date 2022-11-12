package execution.cards.minions;

import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public class MinionCard_Miraj extends MinionCard {
    public MinionCard_Miraj(String description, ArrayList<String> colors,
                            int mana, int ownerIdx, int health, int attackDamage) {
        super("Miraj", description, colors,
                mana, ownerIdx, health, attackDamage,
                false, true, false,
                true, false,
                true, false);
    }

    @Override
    protected Boolean useAbility(Game game, Card card) {
        if (card.getCardType() != 1) {
            // This should never be reached!
            System.out.println("CRITICAL: Can only use ability on minion cards.");
            return false;
        }
        // Swap using xor
        this.health ^= ((MinionCard)card).health;
        ((MinionCard)card).health ^= this.health;
        this.health ^= ((MinionCard)card).health;
        return false;
    }
}
