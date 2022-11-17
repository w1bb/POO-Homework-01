package execution.cards.heros;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public final class HeroCard_KingMudface extends HeroCard {
    public HeroCard_KingMudface(final String description, final ArrayList<String> colors,
                                final int mana, final int ownerIdx) {
        super("King Mudface", description, colors,
                mana, ownerIdx, HeroCard.DEFAULT_HEALTH,
                false, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cards = game.getBoardRow(row);
        for (MinionCard card : cards) {
            if (card != null) {
                card.setHealth(card.getHealth() + 1);
            }
        }
        return ErrorType.NO_ERROR;
    }

    @Override
    public Card copy() {
        return new HeroCard_KingMudface(this.description, this.colors,
                this.mana, this.ownerIdx);
    }
}
