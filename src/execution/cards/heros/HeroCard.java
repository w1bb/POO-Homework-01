package execution.cards.heros;

import execution.cards.Card;

public class HeroCard extends Card {

    public Boolean damage(int damagePoints) {
        this.health = Math.max(0, this.health - damagePoints);
        return (this.health == 0);
    }
}
