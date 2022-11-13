package execution.cards.environments;

import execution.ErrorType;
import execution.Game;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;

public class EnvironmentCard_HeartHound extends EnvironmentCard {
    public EnvironmentCard_HeartHound(String description, ArrayList<String> colors, int mana, int ownerIdx) {
        super("Heart Hound", description, colors,
                mana, ownerIdx,
                true, false);
    }

    @Override
    protected ErrorType useAbility(Game game, int row) {
        MinionCard[] cardsEnemy = game.getBoardRow(row);
        MinionCard[] cardsAlly = game.getBoardRow(3 - row);
        MinionCard cardToMove = null;
        for (MinionCard card : cardsEnemy) {
            if (card != null) {
                if (cardToMove == null)
                    cardToMove = card;
                else if (card.getHealth() > cardToMove.getHealth())
                    cardToMove = card;
            }
        }
        if (cardToMove == null) {
            // This should never be reached!
            return ErrorType.CRITICAL_HEART_HOUND_ABILITY_NO_CARD_TO_MOVE;
        }
        cardToMove.setOwnerIdx(3 - cardToMove.getOwnerIdx());
        // TODO update
        for (int i = 0; i < cardsAlly.length; ++i) {
            if (cardsAlly[i] == null) {
                game.addCardOnBoard(cardToMove, 3 - row, i);
                game.removeCardOnBoard(row, i);
                return null;
            }
        }
        return ErrorType.ERROR_BOARD_ROW_FULL;
    }
}
