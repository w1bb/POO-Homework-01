package execution.cards.minions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.heros.HeroCard;
import fileio.CardInput;

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

    MinionCard(String name, String description, ArrayList<String> colors,
               int mana, int ownerIdx, int health, int attackDamage,
               Boolean tankStatus,
               Boolean allowAttackOnEnemy, Boolean allowAttackOnSelf,
               Boolean allowAbilityOnEnemy, Boolean allowAbilityOnSelf,
               Boolean allowPlacementOnFrontRow, Boolean allowPlacementOnBackRow) {
        super(name, description, colors, mana, 1, ownerIdx);
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

    public void unfreeze() {
        this.frozenStatus = false;
    }

    public void freeze() {
        this.frozenStatus = true;
    }

    public Boolean isFrozen() { return this.frozenStatus; }

    public Boolean isTank() {
        return this.tankStatus;
    }

    public int getHealth() { return this.health; }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public ErrorType tryUseAbility(Game game, Card card) {
        if (card.getCardType() != 1) {
            // This should never be reached!
            return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ATTACK_MINIONS;
        }
        if (this.frozenStatus) {
            return ErrorType.ERROR_ATTACKER_FROZEN;
        }
        if (this.attackCountOnRound > 0 || this.abilityCountOnRound > 0) {
            return ErrorType.ERROR_ATTACKER_ALREADY_ATTACKED;
        }
        if ((this.ownerIdx != card.getOwnerIdx()) && !this.allowAbilityOnEnemy) {
            return ErrorType.ERROR_ATTACKER_NOT_ALLY;
        }
        if (this.allowAbilityOnEnemy && !this.allowAttackOnSelf) {
            if (this.ownerIdx == card.getOwnerIdx()) {
                return ErrorType.ERROR_ATTACKED_CARD_NOT_ENEMY;
            }
            if (game.isTankOnEnemyLane() && !((MinionCard)card).tankStatus) {
                return ErrorType.ERROR_ATTACKED_CARD_NOT_TANK;
            }
        }
        ErrorType returnValue = useAbility(game, card);
        if (returnValue == ErrorType.NO_ERROR)
            ++this.abilityCountOnRound;
        return returnValue;
    }

    protected abstract ErrorType useAbility(Game game, Card card);

    public void damage(int damagePoints) {
        this.health = Math.max(0, this.health - damagePoints);
    }

    public void destroy() {
        this.health = 0;
    }

    public ErrorType tryUseAttack(Game game, Card card) {
        if (card.getCardType() != 0 || card.getCardType() != 1) {
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
        if ((this.ownerIdx != card.getOwnerIdx()) && this.allowAttackOnEnemy && game.isTankOnEnemyLane()) {
            if (card.getCardType() != 1 || (card.getCardType() == 1 && !((MinionCard)card).tankStatus)) {
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

    protected ErrorType useAttack(Card card) {
        if (card.getCardType() == 0) {
            ((HeroCard)card).damage(this.attackDamage);
            return ErrorType.NO_ERROR;
        } else if (card.getCardType() == 1) {
            ((MinionCard)card).damage(this.attackDamage);
            return ErrorType.NO_ERROR;
        }
        // This should never be reached! It might be a little redundant, but better be safe than sorry.
        return ErrorType.CRITICAL_MINIONCARD_CAN_ONLY_ATTACK_MINIONS_HEROS;
    }

    public ObjectNode toObjectNode(ObjectMapper objectMapper) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("mana", this.mana);
        objectNode.put("health", this.health);
        objectNode.put("description", this.description);
        objectNode.put("name", this.name);
        objectNode.put("attackDamage", this.attackDamage);

        ArrayNode colorsNode = objectMapper.createArrayNode();
        for (String color : colors)
            colorsNode.add(color);
        objectNode.set("colors", colorsNode);

        return objectNode;
    }

    public Boolean getAllowPlacementOnFrontRow() { return allowPlacementOnFrontRow; }

    public Boolean getAllowPlacementOnBackRow() { return allowPlacementOnBackRow; }
}
