package execution.cards.heros;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public final class HeroCard_LordRoyce extends HeroCard {
    public HeroCard_LordRoyce(final String description, final ArrayList<String> colors,
                              final int mana, final int ownerIdx) {
        super("Lord Royce", description, colors,
                mana, ownerIdx, HeroCard.DEFAULT_HEALTH,
                true, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cards = game.getBoardRow(row);
        MinionCard cardToChange = null;
        for (MinionCard card : cards) {
            if (card != null) {
                if (cardToChange == null) {
                    cardToChange = card;
                } else if (card.getAttackDamage() > cardToChange.getAttackDamage()) {
                    cardToChange = card;
                }
            }
        }
        if (cardToChange == null) {
            // This should never be reached
            return ErrorType.CRITICAL_LORD_ROYCE_FREEZES_NOTHING;
        }
        cardToChange.freeze();
        return ErrorType.NO_ERROR;
    }

    @Override
    public Card copy() {
        return new HeroCard_LordRoyce(this.description, this.colors, this.mana, this.ownerIdx);
    }
}
