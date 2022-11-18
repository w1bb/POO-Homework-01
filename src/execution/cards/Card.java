package execution.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

/**
 * This class represents a card in the game. Since this isn't very specific, only children of this
 * class can be instantiated.
 */
public abstract class Card {
    protected final String name;
    protected final String description;
    protected final ArrayList<String> colors;
    protected final int mana;
    protected final CardType cardType;
    protected int ownerIdx;

    /**
     * This constructor should only be used by the children when calling super(). It declares most
     * parameters that will represent a card.
     * @param name the name of the card
     * @param description a brief description of the card
     * @param colors the colors found on the card
     * @param mana the mana cost of the card
     * @param cardType the type of card
     * @param ownerIdx the owner's index
     */
    protected Card(final String name, final String description, final ArrayList<String> colors,
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
     * @return the converted value
     */
    public ObjectNode toObjectNode() {
        // Create the ObjectNode that will be returned
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        // Add the "mana", "name" and "description" fields
        objectNode.put("mana", this.mana);
        objectNode.put("name", this.name);
        objectNode.put("description", this.description);
        // Add the "colors" ArrayNode
        ArrayNode colorsNode = objectMapper.createArrayNode();
        for (String color : colors) {
            colorsNode.add(color);
        }
        objectNode.set("colors", colorsNode);
        // Return the constructed ObjectNode
        return objectNode;
    }

    /**
     * This method is custom-made for each card and represents a way to create a deep-copy of a
     * given card.
     * @return a deep-copy of the current card
     */
    public abstract Card copy();
}
