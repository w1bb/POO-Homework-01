package fileio;

import execution.cards.Card;
import execution.cards.environments.EnvironmentCard_Firestorm;
import execution.cards.environments.EnvironmentCard_HeartHound;
import execution.cards.environments.EnvironmentCard_Winterfell;
import execution.cards.heros.HeroCard_EmpressThorina;
import execution.cards.heros.HeroCard_GeneralKocioraw;
import execution.cards.heros.HeroCard_KingMudface;
import execution.cards.heros.HeroCard_LordRoyce;
import execution.cards.minions.*;

import java.util.ArrayList;

public final class CardInput {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;

    public CardInput() {
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Card toCard(final int ownerIdx) {
        switch (name) {
            case "Firestorm":
                return new EnvironmentCard_Firestorm(description, colors, mana, ownerIdx);
            case "Heart Hound":
                return new EnvironmentCard_HeartHound(description, colors, mana, ownerIdx);
            case "Winterfell":
                return new EnvironmentCard_Winterfell(description, colors, mana, ownerIdx);
            case "Empress Thorina":
                return new HeroCard_EmpressThorina(description, colors, mana, ownerIdx);
            case "General Kocioraw":
                return new HeroCard_GeneralKocioraw(description, colors, mana, ownerIdx);
            case "King Mudface":
                return new HeroCard_KingMudface(description, colors, mana, ownerIdx);
            case "Lord Royce":
                return new HeroCard_LordRoyce(description, colors, mana, ownerIdx);
            case "Berserker":
                return new MinionCard_Berserker(description, colors, mana, ownerIdx,
                        health, attackDamage);
            case "Disciple":
                return new MinionCard_Disciple(description, colors, mana, ownerIdx,
                        health);
            case "Goliath":
                return new MinionCard_Goliath(description, colors, mana, ownerIdx,
                        health, attackDamage);
            case "Miraj":
                return new MinionCard_Miraj(description, colors, mana, ownerIdx,
                        health, attackDamage);
            case "Sentinel":
                return new MinionCard_Sentinel(description, colors, mana, ownerIdx,
                        health, attackDamage);
            case "The Cursed One":
                return new MinionCard_TheCursedOne(description, colors, mana, ownerIdx,
                        health);
            case "The Ripper":
                return new MinionCard_TheRipper(description, colors, mana, ownerIdx,
                        health, attackDamage);
            case "Warden":
                return new MinionCard_Warden(description, colors, mana, ownerIdx,
                        health, attackDamage);
            default:
                // This should never be reached!
                System.out.println("CRITICAL: An invalid card was read (" + name + ")!");
        }
        return null;
    }

    @Override
    public String toString() {
        return "CardInput{"
                +  "mana="
                + mana
                +  ", attackDamage="
                + attackDamage
                + ", health="
                + health
                +  ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                +  ""
                + name
                + '\''
                + '}';
    }
}
