package execution.cards.environments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.CardType;
import execution.ErrorType;
import execution.Game;
import execution.Player;
import execution.cards.Card;

import java.util.ArrayList;

public abstract class EnvironmentCard extends Card {
    protected final boolean allowAbilityOnEnemy;
    protected final boolean allowAbilityOnSelf;

    public EnvironmentCard(final String name, final String description,
                           final ArrayList<String> colors, final int mana, final int ownerIdx,
                           final boolean allowAbilityOnEnemy, final boolean allowAbilityOnSelf) {
        super(name, description, colors, mana, CardType.ENVIRONMENT, ownerIdx);
        this.allowAbilityOnEnemy = allowAbilityOnEnemy;
        this.allowAbilityOnSelf = allowAbilityOnSelf;
    }

    public final ErrorType tryUseAbility(final Game game, final int row) {
        Player player = game.getCurrentPlayer();
        if (player.getMana() < this.mana) {
            return ErrorType.ERROR_INSUFFICIENT_MANA_FOR_ENVIRONMENT;
        }
        if (game.isPlayersRow(player, row) && !this.allowAbilityOnSelf) {
            return ErrorType.ERROR_NOT_ENEMY_ROW;
        }
        ErrorType returnValue = useAbility(game, row);
        if (returnValue == ErrorType.NO_ERROR) {
            player.setMana(player.getMana() - this.mana);
            game.redrawBoard();
        }
        return returnValue;
    }

    protected abstract ErrorType useAbility(Game game, int row);

    @Override
    public final ObjectNode toObjectNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("mana", this.mana);
        objectNode.put("name", this.name);
        objectNode.put("description", this.description);

        ArrayNode colorsNode = objectMapper.createArrayNode();
        for (String color : colors) {
            colorsNode.add(color);
        }
        objectNode.set("colors", colorsNode);

        return objectNode;
    }

    public abstract Card copy();
}
