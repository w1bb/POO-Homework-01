package execution.cards;

import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.CardType;

import java.util.ArrayList;

public abstract class Card {
    protected final String name;
    protected final String description;
    protected final ArrayList<String> colors;
    protected final int mana;
    protected final CardType cardType;
    protected int ownerIdx;

    public Card(final String name, final String description, final ArrayList<String> colors,
                final int mana, final CardType cardType, final int ownerIdx) {
        this.name = name;
        this.description = description;
        this.colors = colors;
        this.mana = mana;
        this.cardType = cardType;
        this.ownerIdx = ownerIdx;
    }

    public final CardType getCardType() {
        return this.cardType;
    }

    public final int getOwnerIdx() {
        return this.ownerIdx;
    }

    public final int getMana() {
        return this.mana;
    }

    public final void setOwnerIdx(final int ownerIdx) {
        this.ownerIdx = ownerIdx;
    }
    public abstract ObjectNode toObjectNode();

    public abstract Card copy();
}
