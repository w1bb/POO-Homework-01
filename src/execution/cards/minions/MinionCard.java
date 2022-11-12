package execution.cards.minions;

import execution.Game;
import execution.cards.Card;

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

    protected Boolean frozenStatus;
    protected int attackCountOnRound;
    protected int abilityCountOnRound;

    MinionCard(String name, String description, ArrayList<String> colors,
               int mana, int ownerIdx, int health, int attackDamage,
               Boolean tankStatus,
               Boolean allowAttackOnEnemy, Boolean allowAttackOnSelf,
               Boolean allowAbilityOnEnemy, Boolean allowAbilityOnSelf,
               Boolean allowPlacementOnFrontRow, Boolean allowPlacementOnBackRow) {
        super(name, description, colors, mana, 1, ownerIdx, tankStatus);
        this.health = health;
        this.attackDamage = attackDamage;
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

    public Boolean tryUseAbility(Game game, Card card) {
        if (this.frozenStatus) {
            System.out.println("Attacker card is frozen.");
            return false;
        }
        if (this.attackCountOnRound > 0 || this.abilityCountOnRound > 0) {
            System.out.println("Attacker card has already attacked this turn.");
            return false;
        }
        if ((this.ownerIdx != card.getOwnerIdx()) && !this.allowAbilityOnEnemy && this.allowAttackOnSelf) {
            System.out.println("Attacked card does not belong to the current player.");
            return false;
        }
        if (this.allowAbilityOnEnemy && !this.allowAttackOnSelf) {
            if (this.ownerIdx == card.getOwnerIdx()) {
                System.out.println("Attacked card does not belong to the enemy.");
                return false;
            }
            if (game.isTankOnBoard() && !card.isTank()) {
                System.out.println("Attacked card is not of type 'Tank’.");
                return false;
            }
        }
        Boolean returnValue = useAbility(game, card);
        ++this.abilityCountOnRound;
        return returnValue;
    }

    protected abstract Boolean useAbility(Game game, Card card);
}
