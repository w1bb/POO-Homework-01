package execution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import execution.cards.Card;
import execution.cards.CardType;
import execution.cards.heros.HeroCard;
import execution.cards.minions.MinionCard;

/**
 * This class implements the whole logic of the game. It contains the two players involved, the
 * board and other related info.
 */
public final class Game {
    public static final int BOARD_COLUMNS = 5;
    public static final int BOARD_ROWS = 4;
    public static final int MAX_MANA_INCREMENT = 10;

    private final Player[] players;
    private final int initialPlayer;
    private final MinionCard[][] board;
    private int currentPlayerTurn;
    private int manaToAdd;
    private boolean ended;

    /**
     * This constructor creates a new Game instance.
     * @param players the two players involved in this game
     * @param initialPlayer the index of the first player to play a card
     * @param playerOneDeckIdx the index of player one's deck
     * @param playerTwoDeckIdx the index of player two's deck
     * @param shuffleSeed the seed that will be used when shuffling the cards
     * @param playerOneHeroCard player one's choice of hero card
     * @param playerTwoHeroCard player two's choice of hero card
     */
    public Game(final Player[] players, final int initialPlayer,
                final int playerOneDeckIdx, final int playerTwoDeckIdx,
                final int shuffleSeed,
                final HeroCard playerOneHeroCard, final HeroCard playerTwoHeroCard) {
        this.players = players;
        this.players[0].reset(playerOneDeckIdx, shuffleSeed, playerOneHeroCard);
        this.players[1].reset(playerTwoDeckIdx, shuffleSeed, playerTwoHeroCard);
        this.initialPlayer = initialPlayer;
        this.currentPlayerTurn = initialPlayer;
        this.board = new MinionCard[BOARD_ROWS][BOARD_COLUMNS];
        // Other default values:
        this.manaToAdd = 1;
        this.ended = false;
        // The first round begins
        this.newRound();
    }

    /**
     * This method ends the turn of the current player and lets the other one play. This means all
     * of its cards are unfrozen and could potentially mean a new round will begin.
     */
    public void endTurn() {
        // Each frozen aly minion should be unfrozen
        for (int row = 0; row < board.length; ++row) {
            if (isPlayersRow(players[currentPlayerTurn], row)) {
                for (MinionCard card : board[row]) {
                    if (card != null) {
                        card.unfreeze();
                    }
                }
            }
        }
        // Let the other player play
        currentPlayerTurn = 1 - currentPlayerTurn;
        // Check if both players had their turn, so that a new round would begin
        if (currentPlayerTurn == initialPlayer) {
            this.newRound();
        }
    }

    /**
     * Begin a new round - this means no card has attacked / used its ability yet.
     */
    public void newRound() {
        // Increase each player's mana
        players[0].setMana(players[0].getMana() + manaToAdd);
        players[1].setMana(players[1].getMana() + manaToAdd);
        // Increase nex round's mana portion
        manaToAdd = Math.min(manaToAdd + 1, Game.MAX_MANA_INCREMENT);
        // Draw cards for each player
        players[0].drawCard();
        players[1].drawCard();
        // No abilities / attacks have been spent this round so far, so reset each card's counter
        for (Card[] cards : board) {
            for (Card card : cards) {
                if (card != null) {
                    if (card.getCardType() == CardType.HERO) {
                        ((HeroCard) card).setAbilityCountOnRound(0);
                    } else if (card.getCardType() == CardType.MINION) {
                        ((MinionCard) card).setAttackCountOnRound(0);
                        ((MinionCard) card).setAbilityCountOnRound(0);
                    }
                }
            }
        }
        players[0].getHeroCard().setAbilityCountOnRound(0);
        players[1].getHeroCard().setAbilityCountOnRound(0);
    }

    /**
     * This method returns a certain row in the board.
     * @param row the selected row
     * @return the board's row
     */
    public MinionCard[] getBoardRow(final int row) {
        if (checkRowValidity(row)) {
            return board[row];
        }
        // This should never be reached!
        return null;
    }

    /**
     * This method returns a certain card on the board.
     * @param row the selected row
     * @param column the selected column
     * @return the card found on the board at the given coordinates
     */
    public MinionCard getCard(final int row, final int column) {
        if (checkRowValidity(row) && checkColumnValidity(column)) {
            return board[row][column];
        }
        // This should never be reached!
        return null;
    }

    /**
     * This method checks if a given player is part of the player base.
     * @param player a player to be tested
     * @return <code>true</code> if and only if the player is part of the player base
     */
    public boolean checkPlayerValidity(final Player player) {
        if (player != players[0] && player != players[1]) {
            // This should never be reached!
            System.out.println(ErrorType.CRITICAL_CHECK_PLAYER_VALIDITY_WRONG_PLAYER.interpret());
            return false;
        }
        return true;
    }

    /**
     * This method checks if a given row index is valid.
     * @param row a row index to be tested
     * @return <code>true</code> if and only if the row is valid
     */
    public boolean checkRowValidity(final int row) {
        if (row < 0 || row >= Game.BOARD_ROWS) {
            // This should never be reached!
            System.out.println("CRITICAL: checkRowValidity was called with wrong row ("
                    + row + ")!");
            return false;
        }
        return true;
    }

    /**
     * This method checks if a given column index is valid.
     * @param column a row index to be tested
     * @return <code>true</code> if and only if the column is valid
     */
    public boolean checkColumnValidity(final int column) {
        if (column < 0 || column >= Game.BOARD_COLUMNS) {
            // This should never be reached!
            System.out.println("CRITICAL: checkColumnValidity was called with wrong row ("
                    + column + ")!");
            return false;
        }
        return true;
    }

    /**
     * This method checks if there is a tank on the enemy lanes.
     * @return <code>true</code> if and only if there is a tank on the enemy lanes
     */
    public boolean isTankOnEnemyLanes() {
        for (int row = 0; row < board.length; ++row) {
            // Check only on enemy rows
            if (isPlayersRow(players[1 - currentPlayerTurn], row)) {
                for (MinionCard card : board[row]) {
                    if (card != null && card.isTank()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method checks if a given row is part of a player's row
     * @param player the player to check with
     * @param row the row to be checked
     * @return <code>true</code> if and only if the row is player's
     */
    public boolean isPlayersRow(final Player player, final int row) {
        if (checkPlayerValidity(player) && checkRowValidity(row)) {
            // Let P1 = (player == players[1]) and P2 = (row >= Game.BOARD_ROWS / 2).
            // Let's check out the following truth table:
            // |  [P1]  |  [P2]  |                 Meaning               |
            // | false  | false  | Player one asks if enemy rows are his |
            // | false  |  true  | Player one asks if aly rows are his   |
            // |  true  | false  | Player two asks if aly rows are his   |
            // |  true  |  true  | Player two asks if enemy rows are his |
            // This is why the XOR function would work great.
            return ((player == players[1]) ^ (row >= Game.BOARD_ROWS / 2));
        }
        // This should never be reached
        return false;
    }

    /**
     * This method checks if a given row is part of a player's row
     * @param playerIdx the player index of the player to check with
     * @param row the row to be checked
     * @return <code>true</code> if and only if the row is player's
     */
    public boolean isPlayersRow(final int playerIdx, final int row) {
        return isPlayersRow(players[playerIdx], row);
    }

    /**
     * This method places a minion card on the board at a specified row.
     * @param minionCard the card to be placed
     * @param row the row on which the card should be placed
     * @return based on the success / failure of the push, an ErrorType is issued
     */
    public ErrorType pushOnBoardRow(final MinionCard minionCard, final int row) {
        // Find the nearest empty space
        for (int i = 0; i < board[row].length; ++i) {
            if (board[row][i] == null) {
                board[row][i] = minionCard;
                return ErrorType.NO_ERROR;
            }
        }
        return ErrorType.ERROR_BOARD_ROW_FULL;
    }

    /**
     * This method should be called after any attack that could destroy cards.
     */
    public void redrawBoard() {
        for (int row = 0; row < board.length; ++row) {
            int lastUnusedColumn = 0;
            for (int column = 0; column < Game.BOARD_COLUMNS; ++column) {
                // Consider 0 health cards as nonexistent
                if (board[row][column] != null && board[row][column].getHealth() <= 0) {
                    board[row][column] = null;
                }
                // Fill the gaps
                if (board[row][column] != null) {
                    board[row][lastUnusedColumn] = board[row][column];
                    lastUnusedColumn++;
                }
            }
            // Clear remaining cards
            for (int column = lastUnusedColumn; column < board[row].length; ++column) {
                board[row][column] = null;
            }
        }
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerTurn];
    }

    public Player getNextPlayer() {
        return players[1 - currentPlayerTurn];
    }

    /**
     * This method checks if the game is over or not. A game is considered over once one of the
     * players holds a hero card with 0 health.
     * @return <code>true</code> if and only if at least one player's hero has no more health
     */
    public boolean isOver() {
        if (!ended) {
            if (players[0].getHeroCard().getHealth() <= 0
                && players[1].getHeroCard().getHealth() <= 0) {
                // This should never be reached!
                System.out.println("CRITICAL: Both players are dead!");
                ended = true;
            } else if (players[0].getHeroCard().getHealth() <= 0) {
                // Player two wins
                players[1].addVictory();
                ended = true;
            } else if (players[1].getHeroCard().getHealth() <= 0) {
                // Player one wins
                players[0].addVictory();
                ended = true;
            } else {
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * This method converts the board into a printable ArrayNode format.
     * @return the converted value
     */
    public ArrayNode boardToArrayNode() {
        // Create the ArrayNode that will be returned
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode cardsInside = objectMapper.createArrayNode();
        // Add each card individually
        for (MinionCard[] rowBoard : board) {
            ArrayNode cardsInsideRow = objectMapper.createArrayNode();
            for (MinionCard card : rowBoard) {
                if (card != null) {
                    cardsInsideRow.add(card.toObjectNode());
                }
            }
            cardsInside.add(cardsInsideRow);
        }
        // Return the constructed ArrayNode
        return cardsInside;
    }

    /**
     * This method converts the frozen cards of the board into a printable ArrayNode format.
     * @return the converted value
     */
    public ArrayNode boardFrozenToArrayNode() {
        // Create the ArrayNode that will be returned
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode frozenCardsInside = objectMapper.createArrayNode();
        // Add each frozen card individually
        for (MinionCard[] rowBoard : board) {
            for (MinionCard card : rowBoard) {
                if (card != null && card.isFrozen()) {
                    frozenCardsInside.add(card.toObjectNode());
                }
            }
        }
        // Return the constructed ArrayNode
        return frozenCardsInside;
    }
}
