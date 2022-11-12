package execution.cards.heros;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.Game;
import execution.Player;
import execution.cards.Card;
import execution.cards.minions.MinionCard;
import fileio.CardInput;

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

    public String tryUseAbility(Game game, int row, Player player) {
        if (player.getMana() < this.mana) {
            return "Not enough mana to use hero's ability.";
        }
        if (this.abilityCountOnRound > 0) {
            return "Hero has already attacked this turn.";
        }
        if (game.isPlayersRow(player, row) && !this.allowAbilityOnSelf) {
            return "Selected row does not belong to the enemy.";
        }
        if (!game.isPlayersRow(player, row) && !this.allowAbilityOnEnemy) {
            return "Selected row does not belong to the current player.";
        }
        String returnValue = useAbility(game, row);
        if (returnValue == null)
            ++this.abilityCountOnRound;
        return returnValue;
    }

    protected abstract String useAbility(Game game, int row);

    public ObjectNode toObjectNode(ObjectMapper objectMapper) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("mana", this.mana);
        objectNode.put("health", this.health);
        objectNode.put("description", this.description);
        objectNode.put("name", this.name);

        ArrayNode colorsNode = objectMapper.createArrayNode();
        for (String color : colors)
            colorsNode.add(color);
        objectNode.set("colors", colorsNode);

        return objectNode;
    }
}
