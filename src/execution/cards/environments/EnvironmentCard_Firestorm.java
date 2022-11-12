package execution.cards.environments;

import execution.Game;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public class EnvironmentCard_Firestorm extends EnvironmentCard {
    public EnvironmentCard_Firestorm(String description, ArrayList<String> colors, int mana, int ownerIdx) {
        super("Firestorm", description, colors,
                mana, ownerIdx,
                true, false);
    }

    @Override
    protected Boolean useAbility(Game game, int row) {
        MinionCard[] cards = game.getBoardRow(row);
        boolean returnValue = false;
        for (MinionCard card : cards) {
            if (card != null) {
                card.setHealth(card.getHealth() - 1);
                returnValue |= (card.getHealth() == 0);
            }
        }
        return returnValue;
    }
}
