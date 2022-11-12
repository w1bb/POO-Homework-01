package execution.cards.heros;

import execution.Game;
import execution.cards.minions.MinionCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HeroCard_EmpressThorina extends HeroCard {
    public HeroCard_EmpressThorina(String description, ArrayList<String> colors, int mana, int ownerIdx) {
        super("Empress Thorina", description, colors,
                mana, ownerIdx, 30,
                true, false);
    }

    @Override
    protected Boolean useAbility(Game game, int row) {
        MinionCard[] cards = game.getBoardRow(row);
        MinionCard cardToChange = null;
        for (MinionCard card : cards) {
            if (card != null) {
                if (cardToChange == null)
                    cardToChange = card;
                else if (card.getHealth() > cardToChange.getHealth())
                    cardToChange = card;
            }
        }
        if (cardToChange == null) {
            // This should never be reached
            System.out.println("CRITICAL: Empress Thorina has nothing to attack!");
            return false;
        }
        cardToChange.destroy();
        return true;
    }
}
