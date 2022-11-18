package execution.cards.minions;

import execution.cards.CardType;
import execution.ErrorType;
import execution.Game;
import execution.cards.Card;

import java.util.ArrayList;

/**
 * This class represents a Miraj card.
 */
public final class MinionCard_Miraj extends MinionCard {
    /**
     * This constructor creates a new MinionCard_Miraj (Miraj) card.
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     * @param health the card's original health
     * @param attackDamage the card's original attack damage
     */
    public MinionCard_Miraj(final String description, final ArrayList<String> colors,
                            final int mana, final int ownerIdx, final int health,
                            final int attackDamage) {
        super("Miraj", description, colors,
                mana, ownerIdx, health, attackDamage,
                false, true, false,
                true, false,
                true, false);
    }

    /**
     * {@inheritDoc} For MinionCard_Miraj, the Skyjack ability is used.
     */
    @Override
    protected ErrorType useAbility(final Game game, final Card attackedCard) {
        if (attackedCard.getCardType() != CardType.MINION) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS;
        }
        // Swap attacker health with attacked health using xor (a ^= b; b ^= a; a ^= b)
        this.health ^= ((MinionCard) attackedCard).health;
        ((MinionCard) attackedCard).health ^= this.health;
        this.health ^= ((MinionCard) attackedCard).health;
        return ErrorType.NO_ERROR;
    }

    /**
     * {@inheritDoc}
     * More specifically, an MinionCard_Miraj copy will be created.
     */
    @Override
    public Card copy() {
        return new MinionCard_Miraj(this.description, this.colors, this.mana,
                this.ownerIdx, this.health, this.attackDamage);
    }
}
