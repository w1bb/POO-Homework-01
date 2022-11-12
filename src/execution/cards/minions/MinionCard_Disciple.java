package execution.cards.minions;

import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public class MinionCard_Disciple extends MinionCard {
    public MinionCard_Disciple(String description, ArrayList<String> colors,
                               int mana, int ownerIdx, int health) {
        super("Disciple", description, colors,
                mana, ownerIdx, health, 0,
                false, true, false,
                false, true,
                false, true);
    }

    @Override
    protected Boolean useAbility(Game game, Card card) {
        if (card.getCardType() != 1) {
            // This should never be reached!
            System.out.println("CRITICAL: Can only use ability on minion cards.");
            return false;
        }
        ((MinionCard)card).health += 2;
        return false;
    }
}
