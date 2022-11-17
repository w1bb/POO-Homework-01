package fileio;

import execution.Game;
import execution.Player;
import execution.cards.heros.HeroCard;

public final class StartGameInput {
    private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private int shuffleSeed;
    private CardInput playerOneHero;
    private CardInput playerTwoHero;
    private int startingPlayer;

    public StartGameInput() {
    }

    public int getPlayerOneDeckIdx() {
        return playerOneDeckIdx;
    }

    public void setPlayerOneDeckIdx(final int playerOneDeckIdx) {
        this.playerOneDeckIdx = playerOneDeckIdx;
    }

    public int getPlayerTwoDeckIdx() {
        return playerTwoDeckIdx;
    }

    public void setPlayerTwoDeckIdx(final int playerTwoDeckIdx) {
        this.playerTwoDeckIdx = playerTwoDeckIdx;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public void setShuffleSeed(final int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    public CardInput getPlayerOneHero() {
        return playerOneHero;
    }

    public void setPlayerOneHero(final CardInput playerOneHero) {
        this.playerOneHero = playerOneHero;
    }

    public CardInput getPlayerTwoHero() {
        return playerTwoHero;
    }

    public void setPlayerTwoHero(final CardInput playerTwoHero) {
        this.playerTwoHero = playerTwoHero;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(final int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    /**
     * This method converts the given input format into a Game.
     *
     * @param players an array of two players that will participate in the games
     * @return the converted value
     */
    public Game toGame(final Player[] players) {
        HeroCard playerOneHeroCard = (HeroCard) playerOneHero.toCard(0);
        HeroCard playerTwoHeroCard = (HeroCard) playerTwoHero.toCard(1);
        return new Game(players, startingPlayer - 1, playerOneDeckIdx, playerTwoDeckIdx,
                shuffleSeed, playerOneHeroCard, playerTwoHeroCard);
    }

    @Override
    public String toString() {
        return "StartGameInput{"
                + "playerOneDeckIdx="
                + playerOneDeckIdx
                + ", playerTwoDeckIdx="
                + playerTwoDeckIdx
                + ", shuffleSeed="
                + shuffleSeed
                + ", playerOneHero="
                + playerOneHero
                + ", playerTwoHero="
                + playerTwoHero
                + ", startingPlayer="
                + startingPlayer
                + '}';
    }
}
