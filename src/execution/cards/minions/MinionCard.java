package execution.cards.minions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.CardType;
import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.heros.HeroCard;

import java.util.ArrayList;

public abstract class MinionCard extends Card {
    protected int health;
    protected int attackDamage;
    protected Boolean allowAttackOnEnemy;
    protected Boolean allowAttackOnSelf;
    protected Boolean allowAbilityOnEnemy;
    protected Boolean allowAbilityOnSelf;
    protected Boolean allowPlacementOnFrontRow;
    protected Boolean allowPlacementOnBackRow;
    protected Boolean tankStatus;

    protected Boolean frozenStatus;
    protected int attackCountOnRound;
    protected int abilityCountOnRound;

    MinionCard(final String name, final String description, final ArrayList<String> colors,
               final int mana, final int ownerIdx, final int health, final int attackDamage,
               final Boolean tankStatus,
               final Boolean allowAttackOnEnemy, final Boolean allowAttackOnSelf,
               final Boolean allowAbilityOnEnemy, final Boolean allowAbilityOnSelf,
               final Boolean allowPlacementOnFrontRow, final Boolean allowPlacementOnBackRow) {
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

        this.frozenStatus = false;
        this.attackCountOnRound = 0;
        this.abilityCountOnRound = 0;
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
     * This method is a public wrapper for the protected <code>useAbility()</code> method that
     * makes sure certain conditions are met before using a special ability.
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
            ++this.abilityCountOnRound;
            game.redrawBoard();
        }
        return returnValue;
    }

    /**
     * This method implements a custom-made ability for this minion card.
     * @param game the current game that is played
     * @param attackedCard the card on which the attack is planned
     * @return based on the success / failure of the attack, an ErrorType is issued
     */
    protected abstract ErrorType useAbility(Game game, Card attackedCard);

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

    /**
     * This method is a public wrapper for the protected <code>useAttack()</code> method that
     * makes sure certain conditions are met before using an attack.
     * @param game the current game that is played
     * @param card the card on which the attack is planned
     * @return based on the success / failure of the attack, an ErrorType is issued
     */
    public final ErrorType tryUseAttack(final Game game, final Card card) {
        if (card.getCardType() != CardType.HERO && card.getCardType() != CardType.MINION) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ATTACK_MINIONS_HEROS;
        }
        if (this.frozenStatus) {
            return ErrorType.ERROR_ATTACKER_FROZEN;
        }
        if (this.attackCountOnRound > 0 || this.abilityCountOnRound > 0) {
            return ErrorType.ERROR_ATTACKER_ALREADY_ATTACKED;
        }
        if ((this.ownerIdx == card.getOwnerIdx()) && !this.allowAttackOnSelf) {
            return ErrorType.ERROR_ATTACKED_CARD_NOT_ENEMY;
        }
        if ((this.ownerIdx != card.getOwnerIdx()) && this.allowAttackOnEnemy
                && game.isTankOnEnemyLanes()) {
            if (card.getCardType() != CardType.MINION || !((MinionCard) card).tankStatus) {
                return ErrorType.ERROR_ATTACKED_CARD_NOT_TANK;
            }
        }
        ErrorType returnValue = useAttack(card);
        if (returnValue == ErrorType.NO_ERROR) {
            ++this.attackCountOnRound;
            game.redrawBoard();
        }
        return returnValue;
    }

    /**
     * This method implements a general attack strategy on another card. This may later be
     * overwritten to add complexity in the game.
     * @param card the card on which the attack is planned
     * @return based on the success / failure of the attack, an ErrorType is issued
     */
    protected final ErrorType useAttack(final Card card) {
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

    @Override
    public final ObjectNode toObjectNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("mana", this.mana);
        objectNode.put("health", this.health);
        objectNode.put("description", this.description);
        objectNode.put("name", this.name);
        objectNode.put("attackDamage", this.attackDamage);

        ArrayNode colorsNode = objectMapper.createArrayNode();
        for (String color : colors) {
            colorsNode.add(color);
        }
        objectNode.set("colors", colorsNode);

        return objectNode;
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
     */
    public abstract Card copy();
}
