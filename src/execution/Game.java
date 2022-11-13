package execution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import execution.cards.Card;
import execution.cards.heros.HeroCard;
import execution.cards.minions.MinionCard;

public class Game {
    private Player[] players;
    private int currentRound;
    private int initialPlayer;
    private int currentPlayerTurn;
    private MinionCard[][] board;
    private int manaToAdd;
    private boolean ended;

    public Game(Player[] players, int initialPlayer,
                int playerOneDeckIdx, int playerTwoDeckIdx,
                int shuffleSeed,
                HeroCard playerOneHeroCard, HeroCard playerTwoHeroCard) {
        this.players = players;
        this.players[0].reset(playerOneDeckIdx, shuffleSeed, playerOneHeroCard);
        this.players[1].reset(playerTwoDeckIdx, shuffleSeed, playerTwoHeroCard);
        this.currentRound = 0;
        this.initialPlayer = initialPlayer;
        this.currentPlayerTurn = initialPlayer;
        this.board = new MinionCard[4][5];
        this.manaToAdd = 1;
        this.ended = false;
    }

    public void endTurn() {
        for (int row = 0; row < 3; ++row) {
            if (isPlayersRow(players[currentPlayerTurn], row)) {
                for (MinionCard card : board[row]) {
                    if (card != null)
                        card.unfreeze();
                }
            }
        }
        currentPlayerTurn = 1 - currentPlayerTurn;
        if (currentPlayerTurn == initialPlayer)
            this.newRound();
    }

    public void newRound() {
        players[0].setMana(players[0].getMana() + manaToAdd);
        players[1].setMana(players[1].getMana() + manaToAdd);
        manaToAdd = Math.min(manaToAdd + 1, 10);
        players[0].drawCard();
        players[1].drawCard();
        currentRound++;
    }

    public MinionCard[] getBoardRow(int row) {
        if (checkRowValidity(row))
            return board[row];
        return null;
    }

    public MinionCard getCard(int row, int column) {
        if (checkRowValidity(row) && checkColumnValidity(column))
            return board[row][column];
        return null;
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

    public Boolean isPlayersRow(int playerIdx, int row) {
        return isPlayersRow(players[playerIdx], row);
    }

    public void addCardOnBoard(Card card, int row, int column) {
        if (checkRowValidity(row) && checkColumnValidity(column))
            board[row][column] = ((MinionCard)card);
    }

    public void removeCardOnBoard(int row, int column) {
        if (checkRowValidity(row) && checkColumnValidity(column))
            board[row][column] = null;
    }

    public ErrorType pushOnBoardRow(MinionCard minionCard, int row) {
        for (int i = 0; i < board[row].length; ++i) {
            if (board[row][i] == null) {
                board[row][i] = minionCard;
                return ErrorType.NO_ERROR;
            }
        }
        return ErrorType.ERROR_BOARD_ROW_FULL;
    }

    public void redrawBoard() {
        for (int row = 0; row < 4; ++row) {
            int lastUnusedColumn = 0;
            for (int column = 0; column < 5; ++column) {
                if (board[row][column] != null && board[row][column].getHealth() == 0)
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

    public Player getNextPlayer() {
        return players[1 - currentPlayerTurn];
    }

    public ArrayNode boardToArrayNode(ObjectMapper objectMapper) {
        ArrayNode cardsInside = objectMapper.createArrayNode();
        for (MinionCard[] rowBoard : board)
            for (MinionCard card : rowBoard) {
                if (card != null)
                    cardsInside.add(card.toObjectNode(objectMapper));
                else
                    cardsInside.add("None");
            }
        return cardsInside;
    }

    public ArrayNode boardFrozenToArrayNode(ObjectMapper objectMapper) {
        ArrayNode frozenCardsInside = objectMapper.createArrayNode();
        for (MinionCard[] rowBoard : board)
            for (MinionCard card : rowBoard) {
                if (card != null && card.isFrozen())
                    frozenCardsInside.add(card.toObjectNode(objectMapper));
            }
        return frozenCardsInside;
    }

    public boolean isOver() {
        if (!ended) {
            ended = (players[0].getHeroCard().getHealth() <= 0 ||
                    players[1].getHeroCard().getHealth() <= 0);
            if (ended) {
                if (players[0].getHeroCard().getHealth() > 0)
                    players[0].addVictory();
                else
                    players[1].addVictory();
            }
            return ended;
        }
        return true;
    }
}
