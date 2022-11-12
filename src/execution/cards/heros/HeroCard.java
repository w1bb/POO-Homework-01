package execution.cards.heros;

import execution.Game;
import execution.Player;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public abstract class HeroCard extends Card {
    protected int health;
    protected Boolean allowAbilityOnEnemy;
    protected Boolean allowAbilityOnSelf;
    protected int abilityCountOnRound;

    public HeroCard(String name, String description, ArrayList<String> colors,
                    int mana, int ownerIdx, int health,
                    Boolean allowAbilityOnEnemy, Boolean allowAbilityOnSelf) {
        super(name, description, colors, mana, 0, ownerIdx);
        this.health = health;
        this.allowAbilityOnEnemy = allowAbilityOnEnemy;
        this.allowAbilityOnSelf = allowAbilityOnSelf;

        this.abilityCountOnRound = 0;
    }

    public Boolean damage(int damagePoints) {
        this.health = Math.max(0, this.health - damagePoints);
        return (this.health == 0);
    }

    public Boolean tryUseAbility(Game game, int row, Player player) {
        if (player.getMana() < this.mana) {
            System.out.println("Not enough mana to use hero's ability.");
            return false;
        }
        if (this.abilityCountOnRound > 0) {
            System.out.println("Hero has already attacked this turn.");
            return false;
        }
        if (game.isPlayersRow(player, row) && !this.allowAbilityOnSelf) {
            System.out.println("Selected row does not belong to the enemy.");
            return false;
        }
        if (!game.isPlayersRow(player, row) && !this.allowAbilityOnEnemy) {
            System.out.println("Selected row does not belong to the current player.");
            return false;
        }
        Boolean returnValue = useAbility(game, row);
        ++this.abilityCountOnRound;
        return returnValue;
    }

    protected abstract Boolean useAbility(Game game, int row);
}
