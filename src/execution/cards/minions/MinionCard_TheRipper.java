package execution.cards.minions;

import execution.ErrorType;
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
    protected ErrorType useAbility(Game game, Card card) {
        if (card.getCardType() != 1) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS;
        }
        // Remove 2 AD from the card
        ((MinionCard)card).attackDamage = Math.max(((MinionCard)card).attackDamage - 2, 0);
        return ErrorType.NO_ERROR;
    }
}
