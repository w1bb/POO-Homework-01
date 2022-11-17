package execution.cards.heros;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public final class HeroCard_EmpressThorina extends HeroCard {
    public HeroCard_EmpressThorina(final String description, final ArrayList<String> colors,
                                   final int mana, final int ownerIdx) {
        super("Empress Thorina", description, colors,
                mana, ownerIdx, HeroCard.DEFAULT_HEALTH,
                true, false);
    }

    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cards = game.getBoardRow(row);
        MinionCard cardToChange = null;
        for (MinionCard card : cards) {
            if (card != null) {
                if (cardToChange == null) {
                    cardToChange = card;
                } else if (card.getHealth() > cardToChange.getHealth()) {
                    cardToChange = card;
                }
            }
        }
        if (cardToChange == null) {
            // This should never be reached
            return ErrorType.CRITICAL_EMPRESS_THORINA_ATTACKS_NULL;
        }
        cardToChange.destroy();
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Card copy() {
        return new HeroCard_EmpressThorina(this.description, this.colors,
                this.mana, this.ownerIdx);
    }
}
