package execution.cards.environments;

import execution.ErrorType;
import execution.Game;
import execution.cards.Card;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public final class EnvironmentCard_HeartHound extends EnvironmentCard {
    public EnvironmentCard_HeartHound(final String description, final ArrayList<String> colors,
                                      final int mana, final int ownerIdx) {
        super("Heart Hound", description, colors,
                mana, ownerIdx,
                true, false);
    }

    @Override
    protected ErrorType useAbility(final Game game, final int row) {
        MinionCard[] cardsEnemy = game.getBoardRow(row);
        MinionCard cardToMove = null;
        for (MinionCard card : cardsEnemy) {
            if (card != null) {
                if (cardToMove == null) {
                    cardToMove = card;
                } else if (card.getHealth() > cardToMove.getHealth()) {
                    cardToMove = card;
                }
            }
        }
        if (cardToMove == null) {
            // This should never be reached!
            return ErrorType.CRITICAL_HEART_HOUND_ABILITY_NO_CARD_TO_MOVE;
        }
        cardToMove.setOwnerIdx(game.getRowLength() - cardToMove.getOwnerIdx() - 1);
        return game.pushOnBoardRow(cardToMove, game.getRowLength() - row - 1);
    }

    @Override
    public Card copy() {
        return new EnvironmentCard_HeartHound(this.description, this.colors,
                this.mana, this.ownerIdx);
    }
}
