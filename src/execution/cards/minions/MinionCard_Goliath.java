package execution.cards.minions;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

/**
 * This class represents a Goliath card.
 */
public final class MinionCard_Goliath extends MinionCard {
    /**
     * This constructor creates a new MinionCard_Goliath (Goliath) card.
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     * @param health the card's original health
     * @param attackDamage the card's original attack damage
     */
    public MinionCard_Goliath(final String description, final ArrayList<String> colors,
                              final int mana, final int ownerIdx, final int health,
                              final int attackDamage) {
        super("Goliath", description, colors,
                mana, ownerIdx, health, attackDamage,
                true, true, false,
                false, false,
                true, false);
    }

    /**
     * {@inheritDoc} For MinionCard_Goliath, there is NO special ability.
     */
    @Override
    protected ErrorType useAbility(final Game game, final Card attackedCard) {
        // This should never be reached!
        return ErrorType.CRITICAL_GOLIATH_NO_ABILITY;
    }

    /**
     * {@inheritDoc}
     * More specifically, an MinionCard_Goliath copy will be created.
     */
    @Override
    public Card copy() {
        return new MinionCard_Goliath(this.description, this.colors, this.mana,
                this.ownerIdx, this.health, this.attackDamage);
    }
}
