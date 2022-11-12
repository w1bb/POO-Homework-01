package execution.cards;

import java.util.ArrayList;

public abstract class Card {
    protected final String name;
    protected final String description;
    protected final ArrayList<String> colors;
    protected final int mana;
    protected final int cardType;
    protected int ownerIdx;

    public Card(String name, String description, ArrayList<String> colors,
                int mana, int cardType, int ownerIdx) {
        this.name = name;
        this.description = description;
        this.colors = colors;
        this.mana = mana;
        this.cardType = cardType;
        this.ownerIdx = ownerIdx;
    }

    public int getCardType() {
        return this.cardType;
    }

    public int getOwnerIdx() {
        return this.ownerIdx;
    }

    public void setOwnerIdx(int ownerIdx) {
        this.ownerIdx = ownerIdx;
    }
}
