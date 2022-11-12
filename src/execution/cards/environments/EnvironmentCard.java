package execution.cards.environments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    public String tryUseAbility(Game game, int row) {
        Player player = game.getCurrentPlayer();
        if (player.getMana() < this.mana) {
            return "Not enough mana to use hero's ability.";
        }
        if (game.isPlayersRow(player, row) && !this.allowAbilityOnSelf) {
            return "Chosen row does not belong to the enemy.";
        }
        String returnValue = useAbility(game, row);
        if (returnValue == null)
            player.setMana(player.getMana() - this.mana);
        return returnValue;
    }

    protected abstract String useAbility(Game game, int row);

    @Override
    public ObjectNode toObjectNode(ObjectMapper objectMapper) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("mana", this.mana);
        objectNode.put("name", this.name);
        objectNode.put("description", this.description);

        ArrayNode colorsNode = objectMapper.createArrayNode();
        for (String color : colors)
            colorsNode.add(color);
        objectNode.set("colors", colorsNode);

        return objectNode;
    }
}
