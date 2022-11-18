package execution.cards.heros;

import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.cards.CardType;
import execution.ErrorType;
import execution.Game;
import execution.Player;
import execution.cards.Card;

import java.util.ArrayList;

/**
 * This class represents a hero card in the game, an extension of the original Card class.
 * Still, this is not yet specific enough and should be further extended.
 */
public abstract class HeroCard extends Card {
    protected static final int DEFAULT_HEALTH = 30;

    protected int health;
    protected boolean allowAbilityOnEnemy;
    protected boolean allowAbilityOnSelf;

    protected int abilityCountOnRound;

    /**
     * This constructor should only be used by the children when calling super(). It declares most
     * parameters that will represent an environment card.
     * @param name the name of the card
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     * @param health the card's original health
     * @param allowAbilityOnEnemy a value that is true if the card can use abilities on enemy cards
     * @param allowAbilityOnSelf a value that is true if the card can use abilities on aly cards
     */
    public HeroCard(final String name, final String description, final ArrayList<String> colors,
                    final int mana, final int ownerIdx, final int health,
                    final boolean allowAbilityOnEnemy, final boolean allowAbilityOnSelf) {
        super(name, description, colors, mana, CardType.HERO, ownerIdx);
        this.health = health;
        this.allowAbilityOnEnemy = allowAbilityOnEnemy;
        this.allowAbilityOnSelf = allowAbilityOnSelf;
        // Other default values:
        this.abilityCountOnRound = 0;
    }

    /**
     * This method is a public wrapper for the protected <code>useAbility()</code> method that
     * makes sure certain conditions are met before using a special ability.
     * @param game the current game that is played
     * @param row the row on which the attack is planned
     * @return based on the success / failure of the attack, an ErrorType is issued
     */
    public final ErrorType tryUseAbility(final Game game, final int row, final Player player) {
        // Check if the player has enough mana to play the card
        if (player.getMana() < this.mana) {
            return ErrorType.ERROR_INSUFFICIENT_MANA_FOR_HERO;
        }
        // Only allow an attack or ability per round
        if (this.abilityCountOnRound > 0) {
            return ErrorType.ERROR_HERO_ALREADY_ATTACKED;
        }
        // A card that can only attack another enemy card should only attack an enemy card
        if (game.isPlayersRow(player, row) && !this.allowAbilityOnSelf) {
            return ErrorType.ERROR_SELECTED_ROW_NOT_ENEMY;
        }
        // A card that can only attack another aly card should only attack an aly card
        if (!game.isPlayersRow(player, row) && !this.allowAbilityOnEnemy) {
            return ErrorType.ERROR_SELECTED_ROW_NOT_ALLY;
        }
        // Let the useAbility() method take our place
        ErrorType returnValue = useAbility(game, row);
        if (returnValue == ErrorType.NO_ERROR) {
            // Ability was successfully used
            ++this.abilityCountOnRound;
            player.setMana(player.getMana() - this.mana);
        }
        return returnValue;
    }

    /**
     * This method is custom-made for each environment card and represents its special ability.
     * @param game the current game that is played
     * @param row the row on which the attack is planned
     * @return based on the success / failure of the attack, an ErrorType is issued
     */
    protected abstract ErrorType useAbility(Game game, int row);

    /**
     * This method damages the card, removing at most <code>damagePoints</code> HP.
     *
     * @param damagePoints the most points that should be subtracted from this card's health
     */
    public final void damage(final int damagePoints) {
        this.health = Math.max(0, this.health - damagePoints);
    }

    public final int getHealth() {
        return this.health;
    }

    public final void setAbilityCountOnRound(final int abilityCountOnRound) {
        this.abilityCountOnRound = abilityCountOnRound;
    }

    /**
     * {@inheritDoc}
     * In this context, a HeroCard copy will be made.
     */
    public abstract Card copy();

    /**
     * {@inheritDoc}
     * In this context, a HeroCard will be converted.
     */
    @Override
    public final ObjectNode toObjectNode() {
        // Add the default fields to an ObjectNode
        ObjectNode objectNode = super.toObjectNode();
        // Add the "health" field
        objectNode.put("health", this.health);
        // Return the newly created ObjectNode
        return objectNode;
    }
}
