package execution.cards.environments;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public final class EnvironmentCard_Winterfell extends EnvironmentCard {
    public EnvironmentCard_Winterfell(final String description, final ArrayList<String> colors,
                                      final int mana, final int ownerIdx) {
        super("Winterfell", description, colors,
                mana, ownerIdx,
                true, false);
    }

    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cards = game.getBoardRow(row);
        for (MinionCard card : cards) {
            if (card != null) {
                card.freeze();
            }
        }
        return ErrorType.NO_ERROR;
    }

    @Override
    public Card copy() {
        return new EnvironmentCard_Winterfell(this.description, this.colors,
                this.mana, this.ownerIdx);
    }
}
