package execution.cards.minions;

import execution.ErrorType;
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
    protected ErrorType useAbility(Game game, Card card) {
        if (card.getCardType() != 1) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS;
        }
        ((MinionCard)card).health += 2;
        return ErrorType.NO_ERROR;
    }

    @Override
    public Card copy() {
        return new MinionCard_Disciple(this.description, this.colors, this.mana,
                this.ownerIdx, this.health);
    }
}
