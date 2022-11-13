package execution.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

    public int getMana() { return this.mana; }

    public void setOwnerIdx(int ownerIdx) {
        this.ownerIdx = ownerIdx;
    }
    public abstract ObjectNode toObjectNode(ObjectMapper objectMapper);

    public abstract Card copy();
}
