package execution.cards.heros;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.CardType;
import execution.ErrorType;
import execution.Game;
import execution.Player;
import execution.cards.Card;

import java.util.ArrayList;

public abstract class HeroCard extends Card {
    protected static final int DEFAULT_HEALTH = 30;

    protected int health;
    protected boolean allowAbilityOnEnemy;
    protected boolean allowAbilityOnSelf;

    protected int abilityCountOnRound;

    public HeroCard(final String name, final String description, final ArrayList<String> colors,
                    final int mana, final int ownerIdx, final int health,
                    final boolean allowAbilityOnEnemy, final boolean allowAbilityOnSelf) {
        super(name, description, colors, mana, CardType.HERO, ownerIdx);
        this.health = health;
        this.allowAbilityOnEnemy = allowAbilityOnEnemy;
        this.allowAbilityOnSelf = allowAbilityOnSelf;

        this.abilityCountOnRound = 0;
    }

    public final void damage(final int damagePoints) {
        this.health = Math.max(0, this.health - damagePoints);
    }

    public final ErrorType tryUseAbility(final Game game, final int row, final Player player) {
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

    @Override
    public final ObjectNode toObjectNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("mana", this.mana);
        objectNode.put("health", this.health);
        objectNode.put("description", this.description);
        objectNode.put("name", this.name);

        ArrayNode colorsNode = objectMapper.createArrayNode();
        for (String color : colors) {
            colorsNode.add(color);
        }
        objectNode.set("colors", colorsNode);

        return objectNode;
    }

    public final int getHealth() {
        return this.health;
    }

    public final void setAbilityCountOnRound(final int abilityCountOnRound) {
        this.abilityCountOnRound = abilityCountOnRound;
    }

    public abstract Card copy();
}
