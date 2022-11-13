package execution.cards.heros;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.ErrorType;
import execution.Game;
import execution.Player;
import execution.cards.Card;
import execution.cards.environments.EnvironmentCard_Winterfell;

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

    public ErrorType tryUseAbility(Game game, int row, Player player) {
        if (player.getMana() < this.mana) {
            return ErrorType.ERROR_INSUFFICIENT_MANA_FOR_HERO;
        }
        if (this.abilityCountOnRound > 0) {
            return ErrorType.ERROR_HERO_ALREADY_ATTACKED;
        }
        if (game.isPlayersRow(player, row) && !this.allowAbilityOnSelf) {
            return ErrorType.ERROR_SELECTED_ROW_NOT_ENEMY;
        }
        if (!game.isPlayersRow(player, row) && !this.allowAbilityOnEnemy) {
            return ErrorType.ERROR_SELECTED_ROW_NOT_ALLY;
        }
        ErrorType returnValue = useAbility(game, row);
        if (returnValue == ErrorType.NO_ERROR) {
            ++this.abilityCountOnRound;
            player.setMana(player.getMana() - this.mana);
        }
        return returnValue;
    }

    protected abstract ErrorType useAbility(Game game, int row);

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

    public int getHealth() { return this.health; }

    public void setAbilityCountOnRound(int abilityCountOnRound) {
        this.abilityCountOnRound = abilityCountOnRound;
    }

    public abstract Card copy();
}
