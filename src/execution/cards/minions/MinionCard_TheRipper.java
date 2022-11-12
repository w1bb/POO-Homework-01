package execution.cards.minions;

import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public class MinionCard_TheRipper extends MinionCard {
    public MinionCard_TheRipper(String description, ArrayList<String> colors,
                                int mana, int ownerIdx, int health, int attackDamage) {
        super("The Ripper", description, colors,
                mana, ownerIdx, health, attackDamage,
                false, true, false,
                true, false,
                true, false);
    }

    @Override
    protected String useAbility(Game game, Card card) {
        if (card.getCardType() != 1) {
            // This should never be reached!
            return "CRITICAL: Can only use ability on minion cards.";
        }
        // Remove 2 AD from the card
        ((MinionCard)card).attackDamage = Math.max(((MinionCard)card).attackDamage - 2, 0);
        return null;
    }
}
