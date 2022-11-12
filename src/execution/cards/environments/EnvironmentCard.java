package execution.cards.environments;

import execution.cards.Card;

import java.util.ArrayList;

public class EnvironmentCard extends Card {
    public EnvironmentCard(String name, String description, ArrayList<String> colors, int mana, int ownerIdx) {
        super(name, description, colors, mana, 2, ownerIdx);
    }
}
