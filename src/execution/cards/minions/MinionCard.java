package execution.cards.minions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.CardType;
import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.heros.HeroCard;

import java.util.ArrayList;

/**
 * This class represents a minion card in the game, an extension of the original Card class.
 * Still, this is not yet specific enough and should be further extended.
 */
public abstract class MinionCard extends Card {
    protected int health;
    protected int attackDamage;
    protected boolean allowAttackOnEnemy;
    protected boolean allowAttackOnSelf;
    protected boolean allowAbilityOnEnemy;
    protected boolean allowAbilityOnSelf;
    protected boolean allowPlacementOnFrontRow;
    protected boolean allowPlacementOnBackRow;
    protected boolean tankStatus;

    protected boolean frozenStatus;
    protected int attackCountOnRound;
    protected int abilityCountOnRound;

    /**
     * This constructor should only be used by the children when calling super(). It declares most
     * parameters that will represent an environment card.
     * @param name the name of the card
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     * @param health the card's original health
     * @param attackDamage the car's original attack damage
     * @param allowAttackOnEnemy a value that is true if the card can use attacks on enemy cards
     * @param allowAttackOnSelf a value that is true if the card can use attacks on aly cards
     * @param allowAbilityOnEnemy a value that is true if the card can use abilities on enemy cards
     * @param allowAbilityOnSelf a value that is true if the card can use abilities on aly cards
     * @param allowPlacementOnFrontRow true if the card can be placed on the front row
     * @param allowPlacementOnBackRow true if the card can be placed on the back row
     */
    protected MinionCard(final String name, final String description,
                         final ArrayList<String> colors, final int mana, final int ownerIdx,
                         final int health, final int attackDamage,
                         final boolean tankStatus,
                         final boolean allowAttackOnEnemy, final boolean allowAttackOnSelf,
                         final boolean allowAbilityOnEnemy, final boolean allowAbilityOnSelf,
                         final boolean allowPlacementOnFrontRow,
                         final boolean allowPlacementOnBackRow) {
        super(name, description, colors, mana, CardType.MINION, ownerIdx);
        this.health = health;
        this.attackDamage = attackDamage;
        this.tankStatus = tankStatus;
        this.allowAttackOnEnemy = allowAttackOnEnemy;
        this.allowAttackOnSelf = allowAttackOnSelf;
        this.allowAbilityOnEnemy = allowAbilityOnEnemy;
        this.allowAbilityOnSelf = allowAbilityOnSelf;
        this.allowPlacementOnFrontRow = allowPlacementOnFrontRow;
        this.allowPlacementOnBackRow = allowPlacementOnBackRow;
        // Other default values:
        this.frozenStatus = false;
        this.attackCountOnRound = 0;
        this.abilityCountOnRound = 0;
    }

    /**
     * This method is a public wrapper for the protected <code>useAbility()</code> method that
     * makes sure certain conditions are met before using a special ability.
     *
     * @param game the current game that is played
     * @param card the card on which the attack is planned
     * @return based on the success / failure of the attack, an ErrorType is issued
     */
    public final ErrorType tryUseAbility(final Game game, final Card card) {
        // An ability can only be used on a minion
        if (card.getCardType() != CardType.MINION) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ATTACK_MINIONS;
        }
        // An ability can only be used if the attacker is not frozen
        if (this.frozenStatus) {
            return ErrorType.ERROR_ATTACKER_FROZEN;
        }
        // Only allow an attack or ability per round
        if (this.attackCountOnRound > 0 || this.abilityCountOnRound > 0) {
            return ErrorType.ERROR_ATTACKER_ALREADY_ATTACKED;
        }
        // A card that can only attack another aly card should only attack an aly card
        if ((this.ownerIdx != card.getOwnerIdx()) && !this.allowAbilityOnEnemy) {
            return ErrorType.ERROR_ATTACKER_NOT_ALLY;
        }
        if (this.allowAbilityOnEnemy && !this.allowAttackOnSelf) {
            // A card that can only attack another enemy card should only attack an enemy card
            if (this.ownerIdx == card.getOwnerIdx()) {
                return ErrorType.ERROR_ATTACKED_CARD_NOT_ENEMY;
            }
            // Make sure there are no tanks on the board
            if (game.isTankOnEnemyLanes() && !((MinionCard) card).tankStatus) {
                return ErrorType.ERROR_ATTACKED_CARD_NOT_TANK;
            }
        }
        // Let the useAbility() method take our place
        ErrorType returnValue = useAbility(game, card);
        if (returnValue == ErrorType.NO_ERROR) {
            // Ability was successfully used
            ++this.abilityCountOnRound;
            game.redrawBoard();
        }
        return returnValue;
    }

    /**
     * This method implements a custom-made ability for this minion card.
     *
     * @param game         the current game that is played
     * @param attackedCard the card on which the attack is planned
     * @return based on the success / failure of the attack, an ErrorType is issued
     */
    protected abstract ErrorType useAbility(Game game, Card attackedCard);

    /**
     * This method is a public wrapper for the protected <code>useAttack()</code> method that
     * makes sure certain conditions are met before using an attack.
     *
     * @param game the current game that is played
     * @param card the card on which the attack is planned
     * @return based on the success / failure of the attack, an ErrorType is issued
     */
    public final ErrorType tryUseAttack(final Game game, final Card card) {
        if (card.getCardType() != CardType.HERO && card.getCardType() != CardType.MINION) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ATTACK_MINIONS_HEROS;
        }
        // An attack can only be used if the attacker is not frozen
        if (this.frozenStatus) {
            return ErrorType.ERROR_ATTACKER_FROZEN;
        }
        // Only allow an attack or ability per round
        if (this.attackCountOnRound > 0 || this.abilityCountOnRound > 0) {
            return ErrorType.ERROR_ATTACKER_ALREADY_ATTACKED;
        }
        // A card that can only attack another enemy card should only attack an enemy card
        if ((this.ownerIdx == card.getOwnerIdx()) && !this.allowAttackOnSelf) {
            return ErrorType.ERROR_ATTACKED_CARD_NOT_ENEMY;
        }
        // A card that can only attack another aly card should only attack an aly card
        if ((this.ownerIdx != card.getOwnerIdx()) && this.allowAttackOnEnemy
                && game.isTankOnEnemyLanes()) {
            if (card.getCardType() != CardType.MINION || !((MinionCard) card).tankStatus) {
                return ErrorType.ERROR_ATTACKED_CARD_NOT_TANK;
            }
        }
        // Let the useAttack() method take our place
        ErrorType returnValue = useAttack(card);
        if (returnValue == ErrorType.NO_ERROR) {
            // Attack was successfully used
            ++this.attackCountOnRound;
            game.redrawBoard();
        }
        return returnValue;
    }

    /**
     * This method implements a general attack strategy on another card. This may later be
     * overwritten to add complexity in the game.
     *
     * @param card the card on which the attack is planned
     * @return based on the success / failure of the attack, an ErrorType is issued
     */
    protected final ErrorType useAttack(final Card card) {
        // Call a different damage method based on card's type
        if (card.getCardType() == CardType.HERO) {
            ((HeroCard) card).damage(this.attackDamage);
            return ErrorType.NO_ERROR;
        } else if (card.getCardType() == CardType.MINION) {
            ((MinionCard) card).damage(this.attackDamage);
            return ErrorType.NO_ERROR;
        }
        // This should never be reached! It might be a little redundant,
        // but better be safe than sorry.
        return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ATTACK_MINIONS_HEROS;
    }

    /**
     * This method unfreezes this card (<code>this.frozenStatus = false;</code>).
     */
    public final void unfreeze() {
        this.frozenStatus = false;
    }

    /**
     * This method freezes this card (<code>this.frozenStatus = true;</code>).
     */
    public final void freeze() {
        this.frozenStatus = true;
    }

    public final Boolean isFrozen() {
        return this.frozenStatus;
    }

    public final Boolean isTank() {
        return this.tankStatus;
    }

    public final int getHealth() {
        return this.health;
    }

    public final void setHealth(final int health) {
        this.health = health;
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    public final void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * This method damages the card, removing at most <code>damagePoints</code> HP.
     *
     * @param damagePoints the most points that should be subtracted from this card's health
     */
    public final void damage(final int damagePoints) {
        this.health = Math.max(0, this.health - damagePoints);
    }

    /**
     * This method destroys a given card by changing its health to 0.
     */
    public final void destroy() {
        this.health = 0;
    }

    public final Boolean getAllowPlacementOnFrontRow() {
        return allowPlacementOnFrontRow;
    }

    public final Boolean getAllowPlacementOnBackRow() {
        return allowPlacementOnBackRow;
    }

    public final void setAttackCountOnRound(final int attackCountOnRound) {
        this.attackCountOnRound = attackCountOnRound;
    }

    public final void setAbilityCountOnRound(final int abilityCountOnRound) {
        this.abilityCountOnRound = abilityCountOnRound;
    }

    /**
     * {@inheritDoc}
     * In this context, a MinionCard copy will be made.
     */
    public abstract Card copy();

    /**
     * {@inheritDoc}
     * In this context, a MinionCard will be converted.
     */
    @Override
    public final ObjectNode toObjectNode() {
        // Add the default fields to an ObjectNode
        ObjectNode objectNode = super.toObjectNode();
        // Add the "health" and "attackDamage" fields
        objectNode.put("health", this.health);
        objectNode.put("attackDamage", this.attackDamage);
        // Return the newly created ObjectNode
        return objectNode;
    }
}
