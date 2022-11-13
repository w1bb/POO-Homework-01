package execution.cards.environments;

import execution.ErrorType;
import execution.Game;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public class EnvironmentCard_Winterfell extends EnvironmentCard {
    public EnvironmentCard_Winterfell(String description, ArrayList<String> colors, int mana, int ownerIdx) {
        super("Winterfell", description, colors,
                mana, ownerIdx,
                true, false);
    }

    @Override
    protected ErrorType useAbility(Game game, int row) {
        MinionCard[] cards = game.getBoardRow(row);
        for (MinionCard card : cards) {
            if (card != null) {
                card.freeze();
            }
        }
        return ErrorType.NO_ERROR;
    }
}
