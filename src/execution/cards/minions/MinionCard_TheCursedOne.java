package execution.cards.minions;

import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public class MinionCard_TheCursedOne extends MinionCard {
    public MinionCard_TheCursedOne(String description, ArrayList<String> colors,
                                   int mana, int ownerIdx, int health) {
        super("The Cursed One", description, colors,
                mana, ownerIdx, health, 0,
                false, true, false,
                true, false,
                false, true);
    }

    @Override
    protected Boolean useAbility(Game game, Card card) {
        if (card.getCardType() != 1) {
            // This should never be reached!
            System.out.println("CRITICAL: Can only use ability on minion cards.");
            return false;
        }
        // Swap using xor
        ((MinionCard)card).attackDamage ^= ((MinionCard)card).health;
        ((MinionCard)card).health ^= ((MinionCard)card).attackDamage;
        ((MinionCard)card).attackDamage ^= ((MinionCard)card).health;
        return (((MinionCard)card).health == 0);
    }
}
