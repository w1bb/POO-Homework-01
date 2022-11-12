package execution.cards.minions;

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

    public Boolean tryUseAbility(Game game, Card card) {
        if (card.getCardType() != 1) {
            // This should never be reached!
            System.out.println("CRITICAL: Can only use attack on minion cards.");
            return false;
        }
        if (this.frozenStatus) {
            System.out.println("Attacker card is frozen.");
            return false;
        }
        if (this.attackCountOnRound > 0 || this.abilityCountOnRound > 0) {
            System.out.println("Attacker card has already attacked this turn.");
            return false;
        }
        if ((this.ownerIdx != card.getOwnerIdx()) && !this.allowAbilityOnEnemy) {
            System.out.println("Attacked card does not belong to the current player.");
            return false;
        }
        if (this.allowAbilityOnEnemy && !this.allowAttackOnSelf) {
            if (this.ownerIdx == card.getOwnerIdx()) {
                System.out.println("Attacked card does not belong to the enemy.");
                return false;
            }
            if (game.isTankOnEnemyLane() && !((MinionCard)card).tankStatus) {
                System.out.println("Attacked card is not of type 'Tank’.");
                return false;
            }
        }
        Boolean returnValue = useAbility(game, card);
        ++this.abilityCountOnRound;
        return returnValue;
    }

    protected abstract Boolean useAbility(Game game, Card card);

    public Boolean damage(int damagePoints) {
        this.health = Math.max(0, this.health - damagePoints);
        return (this.health == 0);
    }

    public void destroy() {
        this.health = 0;
    }

    public Boolean tryUseAttack(Game game, Card card) {
        if (card.getCardType() != 0 || card.getCardType() != 1) {
            // This should never be reached!
            System.out.println("CRITICAL: Can only use attack on minion/hero cards.");
            return false;
        }
        if (this.frozenStatus) {
            System.out.println("Attacker card is frozen.");
            return false;
        }
        if (this.attackCountOnRound > 0 || this.abilityCountOnRound > 0) {
            System.out.println("Attacker card has already attacked this turn.");
            return false;
        }
        if ((this.ownerIdx == card.getOwnerIdx()) && !this.allowAttackOnSelf) {
            System.out.println("Attacked card does not belong to the enemy.");
            return false;
        }
        if ((this.ownerIdx != card.getOwnerIdx()) && this.allowAttackOnEnemy && game.isTankOnEnemyLane()) {
            if (card.getCardType() != 1 || (card.getCardType() == 1 && !((MinionCard)card).tankStatus)) {
                System.out.println("Attacked card is not of type 'Tank’.");
                return false;
            }
        }
        Boolean returnValue = useAttack(card);
        ++this.attackCountOnRound;
        return returnValue;
    }

    protected Boolean useAttack(Card card) {
        if (card.getCardType() == 0) {
            return ((HeroCard)card).damage(this.attackDamage);
        } else if (card.getCardType() == 1) {
            return ((MinionCard)card).damage(this.attackDamage);
        } else {
            // This should never be reached! It might be a little redundant, but better be safe than sorry.
            System.out.println("CRITICAL: Can only use attack on minion/hero cards.");
            return false;
        }
    }
}
