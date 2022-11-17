package execution.cards.minions;

import execution.CardType;
import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

public final class MinionCard_TheRipper extends MinionCard {
    public MinionCard_TheRipper(final String description, final ArrayList<String> colors,
                                final int mana, final int ownerIdx, final int health,
                                final int attackDamage) {
        super("The Ripper", description, colors,
                mana, ownerIdx, health, attackDamage,
                false, true, false,
                true, false,
                true, false);
    }

    @Override
    protected ErrorType useAbility(final Game game, final Card attackedCard) {
        if (attackedCard.getCardType() != CardType.MINION) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS;
        }
        // Remove 2 AD from the card
        ((MinionCard) attackedCard).attackDamage =
                Math.max(((MinionCard) attackedCard).attackDamage - 2, 0);
        return ErrorType.NO_ERROR;
    }

    @Override
    public Card copy() {
        return new MinionCard_TheRipper(this.description, this.colors, this.mana,
                this.ownerIdx, this.health, this.attackDamage);
    }
}
