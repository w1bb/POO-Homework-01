package execution.cards.heros;

import execution.Game;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HeroCard_KingMudface extends HeroCard {
    public HeroCard_KingMudface(String description, ArrayList<String> colors, int mana, int ownerIdx) {
        super("King Mudface", description, colors,
                mana, ownerIdx, 30,
                false, true);
    }

    @Override
    protected String useAbility(Game game, int row) {
        MinionCard[] cards = game.getBoardRow(row);
        for (MinionCard card : cards) {
            if (card != null) {
                card.setHealth(card.getHealth() + 1);
            }
        }
        return null;
    }
}
