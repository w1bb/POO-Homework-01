package execution.cards;

import java.util.ArrayList;

public abstract class Card {
    protected final String name;
    protected final String description;
    protected final ArrayList<String> colors;
    protected final int mana;
    protected final int cardType;
    protected final int ownerIdx;
    protected Boolean tankStatus;

    public Card(String name, String description, ArrayList<String> colors,
                int mana, int cardType, int ownerIdx, Boolean tankStatus) {
        this.name = name;
        this.description = description;
        this.colors = colors;
        this.mana = mana;
        this.cardType = cardType;
        this.ownerIdx = ownerIdx;
        this.tankStatus = tankStatus;
    }

    public int getCardType() {
        return this.cardType;
    }

    public int getOwnerIdx() {
        return this.ownerIdx;
    }

    public Boolean isTank() {
        return this.tankStatus;
    }
}
