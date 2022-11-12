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

    public Boolean checkPlayerValidity(Player player) {
        if (player != players[0] && player != players[1]) {
            // This should never be reached!
            System.out.println("CRITICAL: checkPlayerValidity was called with wrong player!");
            return false;
        }
        return true;
    }

    public Boolean checkRowValidity(int row) {
        if (row < 0 || row > 3) {
            // This should never be reached!
            System.out.println("CRITICAL: checkRowValidity was called with wrong row (" + row + ")!");
            return false;
        }
        return true;
    }

    public Boolean checkColumnValidity(int column) {
        if (column < 0 || column > 4) {
            // This should never be reached!
            System.out.println("CRITICAL: checkColumnValidity was called with wrong row (" + column + ")!");
            return false;
        }
        return true;
    }

    public Boolean isTankOnEnemyLane() {
        for (int row = 0; row < 3; ++row) {
            if (!isPlayersRow(players[currentPlayerTurn], row)) {
                for (MinionCard card : board[row]) {
                    if (card.isTank())
                        return true;
                }
            }
        }
        return false;
    }

    public Boolean isPlayersRow(Player player, int row) {
        if (checkPlayerValidity(player) && checkRowValidity(row))
            return (player == players[1]) ^ (initialPlayer == 1) ^ (row < 2);
        return null;
    }

    public void addCardOnBoard(Card card, int row, int column) {
        if (checkRowValidity(row) && checkColumnValidity(column))
            board[row][column] = ((MinionCard)card);
    }

    public void removeCardOnBoard(int row, int column) {
        if (checkRowValidity(row) && checkColumnValidity(column))
            board[row][column] = null;
    }

    public void redrawBoard() {
        for (int row = 0; row < 4; ++row) {
            int lastUnusedColumn = 0;
            for (int column = 0; column < 5; ++column) {
                if (board[row][column].getHealth() == 0)
                    board[row][column] = null;
                if (board[row][column] != null) {
                    board[row][lastUnusedColumn] = board[row][column];
                    board[row][column] = null;
                    lastUnusedColumn++;
                }
            }
        }
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerTurn];
    }
}
