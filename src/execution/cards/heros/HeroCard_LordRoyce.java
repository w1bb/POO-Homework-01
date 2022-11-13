package execution.cards.heros;

import execution.ErrorType;
import execution.Game;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HeroCard_LordRoyce extends HeroCard {
    public HeroCard_LordRoyce(String description, ArrayList<String> colors, int mana, int ownerIdx) {
        super("Lord Royce", description, colors,
                mana, ownerIdx, 30,
                true, false);
    }

    @Override
    protected ErrorType useAbility(Game game, int row) {
        MinionCard[] cards = game.getBoardRow(row);
        MinionCard cardToChange = null;
        for (MinionCard card : cards) {
            if (card != null) {
                if (cardToChange == null)
                    cardToChange = card;
                else if (card.getAttackDamage() > cardToChange.getAttackDamage())
                    cardToChange = card;
            }
        }
        if (cardToChange == null) {
            // This should never be reached
            return ErrorType.CRITICAL_LORD_ROYCE_FREEZES_NOTHING;
        }
        cardToChange.freeze();
        return ErrorType.NO_ERROR;
    }
}
