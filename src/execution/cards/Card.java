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

    /**
     * This method converts the given format into a printable ObjectNode format.
     *
     * @return the converted value
     */
    public abstract ObjectNode toObjectNode();

    /**
     * This method is custom-made for each card and represents a way to create a deep-copy of a
     * given card
     * @return a deep-copy of the current card
     */
    public abstract Card copy();
}
