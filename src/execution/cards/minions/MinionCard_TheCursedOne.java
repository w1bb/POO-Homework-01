package execution.cards.minions;

import execution.ErrorType;
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
    protected ErrorType useAbility(Game game, Card card) {
        if (card.getCardType() != 1) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS;
        }
        // Swap using xor
        ((MinionCard)card).attackDamage ^= ((MinionCard)card).health;
        ((MinionCard)card).health ^= ((MinionCard)card).attackDamage;
        ((MinionCard)card).attackDamage ^= ((MinionCard)card).health;
        return ErrorType.NO_ERROR;
    }

    @Override
    public Card copy() {
        return new MinionCard_TheCursedOne(this.description, this.colors, this.mana,
                this.ownerIdx, this.health);
    }
}
