package execution.cards.heros;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public final class HeroCard_GeneralKocioraw extends HeroCard {
    public HeroCard_GeneralKocioraw(final String description, final ArrayList<String> colors,
                                    final int mana, final int ownerIdx) {
        super("General Kocioraw", description, colors,
                mana, ownerIdx, HeroCard.DEFAULT_HEALTH,
                false, true);
    }

    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cards = game.getBoardRow(row);
        for (MinionCard card : cards) {
            if (card != null) {
                card.setAttackDamage(card.getAttackDamage() + 1);
            }
        }
        return ErrorType.NO_ERROR;
    }

    @Override
    public Card copy() {
        return new HeroCard_GeneralKocioraw(this.description, this.colors,
                this.mana, this.ownerIdx);
    }
}
