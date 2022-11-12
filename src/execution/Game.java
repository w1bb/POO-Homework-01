package execution;

import execution.cards.Card;
import execution.cards.heros.HeroCard;
import execution.cards.minions.MinionCard;

public class Game {
    private Player[] players;
    private int currentRound;
    private int currentPlayerTurn;
    private int initialPlayer;
    private MinionCard[][] board;

    public Game() {
        board = new MinionCard[4][5];
    }

    public MinionCard[] getBoardRow(int row) {
        if (row < 0 || row > 3) {
            // This should never be reached!
            System.out.println("CRITICAL: getBoardRow(" + row + ") was called!");
            return null;
        }
        return board[row];
    }


    public Boolean isTankOnBoard() {
        return false; // TODO
    }

    public Boolean isPlayersRow(Player player, int row) {
        if (player != players[0] && player != players[1]) {
            // This should never be reached!
            System.out.println("CRITICAL: isPlayersRow was called with wrong player!");
            return null;
        }
        if (row < 0 || row > 3) {
            // This should never be reached!
            System.out.println("CRITICAL: isPlayersRow was called with wrong row (" + row + ")!");
            return null;
        }
        return (player == players[1]) ^ (initialPlayer == 1) ^ (row < 2);
    }
}
