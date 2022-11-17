package execution.cards.minions;

import execution.CardType;
import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public final class MinionCard_Disciple extends MinionCard {
    public MinionCard_Disciple(final String description, final ArrayList<String> colors,
                               final int mana, final int ownerIdx, final int health) {
        super("Disciple", description, colors,
                mana, ownerIdx, health, 0,
                false, true, false,
                false, true,
                false, true);
    }

    /**
     * {@inheritDoc} For MinionCard_Disciple, the God's Plan ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final Card attackedCard) {
        if (attackedCard.getCardType() != CardType.MINION) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS;
        }
        ((MinionCard) attackedCard).health += 2;
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     * More specifically, an MinionCard_Disciple copy will be created.
     */
    @Override
    public Card copy() {
        return new MinionCard_Disciple(this.description, this.colors, this.mana,
                this.ownerIdx, this.health);
    }
}
