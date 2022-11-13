package execution.cards.environments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.ErrorType;
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

    public ErrorType tryUseAbility(Game game, int row) {
        Player player = game.getCurrentPlayer();
        if (player.getMana() < this.mana)
            return ErrorType.ERROR_INSUFFICIENT_MANA_FOR_ENVIRONMENT;
        if (game.isPlayersRow(player, row) && !this.allowAbilityOnSelf)
            return ErrorType.ERROR_NOT_ENEMY_ROW;
        ErrorType returnValue = useAbility(game, row);
        if (returnValue == ErrorType.NO_ERROR) {
            player.setMana(player.getMana() - this.mana);
            game.redrawBoard();
        }
        return returnValue;
    }

    protected abstract ErrorType useAbility(Game game, int row);

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
