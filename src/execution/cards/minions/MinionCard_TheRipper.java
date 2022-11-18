package execution.cards.minions;

import execution.cards.CardType;
import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

/**
 * This class represents The Ripper card.
 */
public final class MinionCard_TheRipper extends MinionCard {
    /**
     * This constructor creates a new MinionCard_TheRipper (The Ripper) card.
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     * @param health the card's original health
     * @param attackDamage the card's original attack damage
     */
    public MinionCard_TheRipper(final String description, final ArrayList<String> colors,
                                final int mana, final int ownerIdx, final int health,
                                final int attackDamage) {
        super("The Ripper", description, colors,
                mana, ownerIdx, health, attackDamage,
                false, true, false,
                true, false,
                true, false);
    }

    /**
     * {@inheritDoc} For MinionCard_TheRipper, the Weak Knees ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final Card attackedCard) {
        final int abilityADExtract = 2;
        // Check if the card is a minion
        if (attackedCard.getCardType() != CardType.MINION) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS;
        }
        // Remove ABILITY_AD_EXTRACT AD from the card
        ((MinionCard) attackedCard).attackDamage =
                Math.max(((MinionCard) attackedCard).attackDamage - abilityADExtract, 0);
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     * More specifically, an MinionCard_TheRipper copy will be created.
     */
    @Override
    public Card copy() {
        return new MinionCard_TheRipper(this.description, this.colors, this.mana,
                this.ownerIdx, this.health, this.attackDamage);
    }
}
