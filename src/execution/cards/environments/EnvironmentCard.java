package execution.cards.environments;

import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.CardType;
import execution.ErrorType;
import execution.Game;
import execution.Player;
import execution.cards.Card;

import java.util.ArrayList;

/**
 * This class represents an environment card in the game, an extension of the original Card class.
 * Still, this is not yet specific enough and should be further extended.
 */
public abstract class EnvironmentCard extends Card {
    protected final boolean allowAbilityOnEnemy;
    protected final boolean allowAbilityOnSelf;

    /**
     * This constructor should only be used by the children when calling super(). It declares most
     * parameters that will represent an environment card.
     * @param name the name of the card
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param ownerIdx the owner's index
     * @param allowAbilityOnEnemy a value that is true if the card can use abilities on enemy cards
     * @param allowAbilityOnSelf a value that is true if the card can use abilities on aly cards
     */
    protected EnvironmentCard(final String name, final String description,
                              final ArrayList<String> colors, final int mana, final int ownerIdx,
                              final boolean allowAbilityOnEnemy,
                              final boolean allowAbilityOnSelf) {
        super(name, description, colors, mana, CardType.ENVIRONMENT, ownerIdx);
        this.allowAbilityOnEnemy = allowAbilityOnEnemy;
        this.allowAbilityOnSelf = allowAbilityOnSelf;
    }

    /**
     * This method is a public wrapper for the protected <code>useAbility()</code> method that
     * makes sure certain conditions are met before using a special ability.
     * @param game the current game that is played
     * @param row the row on which the attack is planned
     * @return based on the success / failure of the attack, an ErrorType is issued
     */
    public final ErrorType tryUseAbility(final Game game, final int row) {
        Player player = game.getCurrentPlayer();
        // Check if the player has enough mana to play the card
        if (player.getMana() < this.mana) {
            return ErrorType.ERROR_INSUFFICIENT_MANA_FOR_ENVIRONMENT;
        }
        // Check if the player can use ability on self row
        if (game.isPlayersRow(player, row) && !this.allowAbilityOnSelf) {
            return ErrorType.ERROR_NOT_ENEMY_ROW;
        }
        // Let the useAbility() method take our place
        ErrorType returnValue = useAbility(game, row);
        if (returnValue == ErrorType.NO_ERROR) {
            // Ability was successfully used
            player.setMana(player.getMana() - this.mana);
            game.redrawBoard();
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
     * {@inheritDoc}
     * In this context, an EnvironmentCard copy will be made.
     */
    public abstract Card copy();

    /**
     * {@inheritDoc}
     * In this context, an EnvironmentCard will be converted.
     */
    @Override
    public final ObjectNode toObjectNode() {
        // Everything is already implemented
        return super.toObjectNode();
    }
}
