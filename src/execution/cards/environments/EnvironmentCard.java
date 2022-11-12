package execution.cards.environments;

import execution.Game;
import execution.Player;
import execution.cards.Card;

import java.util.ArrayList;

public abstract class EnvironmentCard extends Card {
    protected Boolean allowAbilityOnEnemy;
    protected Boolean allowAbilityOnSelf;

    public EnvironmentCard(String name, String description, ArrayList<String> colors, int mana, int ownerIdx,
                           Boolean allowAbilityOnEnemy, Boolean allowAbilityOnSelf) {
        super(name, description, colors, mana, 2, ownerIdx);
        this.allowAbilityOnEnemy = allowAbilityOnEnemy;
        this.allowAbilityOnSelf = allowAbilityOnSelf;
    }

    public Boolean tryUseAbility(Game game, int row) {
        Player player = game.getCurrentPlayer();
        if (player.getMana() < this.mana) {
            System.out.println("Not enough mana to use hero's ability.");
            return false;
        }
        if (game.isPlayersRow(player, row) && !this.allowAbilityOnSelf) {
            System.out.println("Chosen row does not belong to the enemy.");
            return false;
        }
        return useAbility(game, row);
    }

    protected abstract Boolean useAbility(Game game, int row);
}
