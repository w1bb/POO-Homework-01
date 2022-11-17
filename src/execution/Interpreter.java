package execution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.cards.Card;
import execution.cards.environments.EnvironmentCard;
import execution.cards.heros.HeroCard;
import execution.cards.minions.MinionCard;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;

import java.util.ArrayList;

public final class Interpreter {
    private final Player[] players;
    private final ArrayList<GameInput> gameInputs;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    private int currentGame;

    public Interpreter(final Input inputData) {
        this.players = new Player[2];
        this.players[0] = new Player(0, inputData.getPlayerOneDecks().toArrayOfDeck(0));
        this.players[1] = new Player(1, inputData.getPlayerTwoDecks().toArrayOfDeck(1));
        this.gameInputs = inputData.getGames();
        this.objectMapper = new ObjectMapper();
        this.currentGame = 0;
        this.output = this.objectMapper.createArrayNode();
    }

    /**
     * This method interprets the "getCardsInHand" command.
     */
    private void interpretGetCardsInHand(final ActionsInput actionsInput,
                                         final ObjectNode objectNode) {
        objectNode.put("playerIdx", actionsInput.getPlayerIdx());
        objectNode.set("output",
                players[actionsInput.getPlayerIdx() - 1].currentHandToArrayNode());
    }

    /**
     * This method interprets the "getPlayerDeck" command.
     */
    private void interpretGetPlayerDeck(final ActionsInput actionsInput,
                                        final ObjectNode objectNode) {
        objectNode.put("playerIdx", actionsInput.getPlayerIdx());
        objectNode.set("output",
                players[actionsInput.getPlayerIdx() - 1].getCurrentDeck().toArrayNode());
    }

    /**
     * This method interprets the "getCardsOnTable" command.
     */
    private void interpretGetCardsOnTable(final Game game,
                                          final ObjectNode objectNode) {
        objectNode.set("output", game.boardToArrayNode());
    }

    /**
     * This method interprets the "getPlayerTurn" command.
     */
    private void interpretGetPlayerTurn(final Game game,
                                        final ObjectNode objectNode) {
        objectNode.put("output",
                (game.getCurrentPlayer() == players[0]) ? 1 : 2);
    }

    /**
     * This method interprets the "getPlayerHero" command.
     */
    private void interpretGetPlayerHero(final ActionsInput actionsInput,
                                        final ObjectNode objectNode) {
        objectNode.put("playerIdx", actionsInput.getPlayerIdx());
        objectNode.set("output",
                players[actionsInput.getPlayerIdx() - 1].getHeroCard().toObjectNode());
    }

    /**
     * This method interprets the "getCardAtPosition" command.
     */
    private void interpretGetCardAtPosition(final ActionsInput actionsInput,
                                            final Game game,
                                            final ObjectNode objectNode) {
        objectNode.put("x", actionsInput.getX());
        objectNode.put("y", actionsInput.getY());
        MinionCard card = game.getCard(actionsInput.getX(), actionsInput.getY());
        if (card == null) {
            objectNode.put("output", "No card available at that position.");
        } else {
            objectNode.set("output", card.toObjectNode());
        }
    }

    /**
     * This method interprets the "getPlayerMana" command.
     */
    private void interpretGetPlayerMana(final ActionsInput actionsInput,
                                        final ObjectNode objectNode) {
        objectNode.put("playerIdx", actionsInput.getPlayerIdx());
        objectNode.put("output", players[actionsInput.getPlayerIdx() - 1].getMana());
    }

    /**
     * This method interprets the "getEnvironmentCardsInHand" command.
     */
    private void interpretGetEnvironmentCardsInHand(final ActionsInput actionsInput,
                                                    final ObjectNode objectNode) {
        objectNode.put("playerIdx", actionsInput.getPlayerIdx());
        objectNode.set("output",
                players[actionsInput.getPlayerIdx() - 1].currentHandEnvironmentToArrayNode());
    }

    /**
     * This method interprets the "getFrozenCardsOnTable" command.
     */
    private void interpretGetFrozenCardsOnTable(final Game game,
                                                final ObjectNode objectNode) {
        objectNode.set("output", game.boardFrozenToArrayNode());
    }

    /**
     * This method interprets the "getTotalGamesPlayed" command.
     */
    private void interpretGetTotalGamesPlayed(final ObjectNode objectNode) {
        objectNode.put("output", this.currentGame + 1);
    }

    /**
     * This method interprets the "getPlayerOneWins" command.
     */
    private void interpretGetPlayerOneWins(final ObjectNode objectNode) {
        objectNode.put("output", players[0].getWins());
    }

    /**
     * This method interprets the "getPlayerTwoWins" command.
     */
    private void interpretGetPlayerTwoWins(final ObjectNode objectNode) {
        objectNode.put("output", players[1].getWins());
    }

    /**
     * This method interprets the debug commands.
     * @param actionsInput the given input to be parsed
     * @param game the current game
     * @return <code>true</code> if and only if the command was a debug command
     */
    private boolean interpretDebugAction(final ActionsInput actionsInput,
                                         final Game game) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", actionsInput.getCommand());

        switch (actionsInput.getCommand()) {
            case "getCardsInHand":
                interpretGetCardsInHand(actionsInput, objectNode);
                break;
            case "getPlayerDeck":
                interpretGetPlayerDeck(actionsInput, objectNode);
                break;
            case "getCardsOnTable":
                interpretGetCardsOnTable(game, objectNode);
                break;
            case "getPlayerTurn":
                interpretGetPlayerTurn(game, objectNode);
                break;
            case "getPlayerHero":
                interpretGetPlayerHero(actionsInput, objectNode);
                break;
            case "getCardAtPosition":
                interpretGetCardAtPosition(actionsInput, game, objectNode);
                break;
            case "getPlayerMana":
                interpretGetPlayerMana(actionsInput, objectNode);
                break;
            case "getEnvironmentCardsInHand":
                interpretGetEnvironmentCardsInHand(actionsInput, objectNode);
                break;
            case "getFrozenCardsOnTable":
                interpretGetFrozenCardsOnTable(game, objectNode);
                break;
            case "getTotalGamesPlayed":
                interpretGetTotalGamesPlayed(objectNode);
                break;
            case "getPlayerOneWins":
                interpretGetPlayerOneWins(objectNode);
                break;
            case "getPlayerTwoWins":
                interpretGetPlayerTwoWins(objectNode);
                break;
            default:
                return false;
        }

        output.add(objectNode);
        return true;
    }

    /**
     * This method interprets the "endPlayerTurn" command.
     */
    private void interpretEndPlayerTurn(final Game game) {
        game.endTurn();
    }

    /**
     * This method interprets the "placeCard" command.
     */
    private void interpretPlaceCard(final ActionsInput actionsInput,
                                    final Game game,
                                    final ObjectNode objectNode) {
        ErrorType e = game.getCurrentPlayer().placeCardOnBoard(actionsInput.getHandIdx(), game);
        if (e != ErrorType.NO_ERROR) {
            objectNode.put("error", e.interpret());
            objectNode.put("handIdx", actionsInput.getHandIdx());
            output.add(objectNode);
        }
    }

    /**
     * This method interprets the "cardUsesAttack" command.
     */
    private void interpretCardUsesAttack(final ActionsInput actionsInput,
                                         final Game game,
                                         final ObjectNode objectNode) {
        int attackerY = actionsInput.getCardAttacker().getY();
        int attackerX = actionsInput.getCardAttacker().getX();
        MinionCard attackerCard = game.getCard(attackerX, attackerY);
        int attackedY = actionsInput.getCardAttacked().getY();
        int attackedX = actionsInput.getCardAttacked().getX();
        Card attackedCard = game.getCard(attackedX, attackedY);
        ErrorType e = attackerCard.tryUseAttack(game, attackedCard);
        if (e != ErrorType.NO_ERROR) {
            ObjectNode cardAttacker = objectMapper.createObjectNode();
            cardAttacker.put("x", attackerX);
            cardAttacker.put("y", attackerY);
            ObjectNode cardAttacked = objectMapper.createObjectNode();
            cardAttacked.put("x", attackedX);
            cardAttacked.put("y", attackedY);
            objectNode.set("cardAttacker", cardAttacker);
            objectNode.set("cardAttacked", cardAttacked);
            objectNode.put("error", e.interpret());
            output.add(objectNode);
        }
        game.redrawBoard();
    }

    /**
     * This method interprets the "cardUsesAbility" command.
     */
    private void interpretCardUsesAbility(final ActionsInput actionsInput,
                                          final Game game,
                                          final ObjectNode objectNode) {
        int attackerY = actionsInput.getCardAttacker().getY();
        int attackerX = actionsInput.getCardAttacker().getX();
        MinionCard attackerCard = game.getCard(attackerX, attackerY);
        int attackedY = actionsInput.getCardAttacked().getY();
        int attackedX = actionsInput.getCardAttacked().getX();
        MinionCard attackedCard = game.getCard(attackedX, attackedY);
        ErrorType e = attackerCard.tryUseAbility(game, attackedCard);
        if (e != ErrorType.NO_ERROR) {
            ObjectNode cardAttacker = objectMapper.createObjectNode();
            cardAttacker.put("x", attackerX);
            cardAttacker.put("y", attackerY);
            ObjectNode cardAttacked = objectMapper.createObjectNode();
            cardAttacked.put("x", attackedX);
            cardAttacked.put("y", attackedY);
            objectNode.set("cardAttacker", cardAttacker);
            objectNode.set("cardAttacked", cardAttacked);
            objectNode.put("error", e.interpret());
            output.add(objectNode);
        }
        game.redrawBoard();
    }

    /**
     * This method interprets the "useAttackHero" command.
     */
    private void interpretUseAttackHero(final ActionsInput actionsInput,
                                        final Game game,
                                        final ObjectNode objectNode) {
        int attackerY = actionsInput.getCardAttacker().getY();
        int attackerX = actionsInput.getCardAttacker().getX();
        MinionCard attackerCard = game.getCard(attackerX, attackerY);
        HeroCard attackedCard = game.getNextPlayer().getHeroCard();
        ErrorType e = attackerCard.tryUseAttack(game, attackedCard);
        if (e != ErrorType.NO_ERROR) {
            ObjectNode cardAttacker = objectMapper.createObjectNode();
            cardAttacker.put("x", attackerX);
            cardAttacker.put("y", attackerY);
            objectNode.set("cardAttacker", cardAttacker);
            objectNode.put("error", e.interpret());
            output.add(objectNode);
        } else if (game.isOver()) {
            objectNode.removeAll();
            if (players[0].getHeroCard().getHealth() > 0) {
                objectNode.put("gameEnded", "Player one killed the enemy hero.");
                output.add(objectNode);
            } else {
                objectNode.put("gameEnded", "Player two killed the enemy hero.");
                output.add(objectNode);
            }
        }
        game.redrawBoard();
    }

    /**
     * This method interprets the "useHeroAbility" command.
     */
    private void interpretUseHeroAbility(final ActionsInput actionsInput,
                                         final Game game,
                                         final ObjectNode objectNode) {
        int row = actionsInput.getAffectedRow();
        HeroCard attackerCard = game.getCurrentPlayer().getHeroCard();
        ErrorType e = attackerCard.tryUseAbility(game, row, game.getCurrentPlayer());
        if (e != ErrorType.NO_ERROR) {
            objectNode.put("affectedRow", actionsInput.getAffectedRow());
            objectNode.put("error", e.interpret());
            output.add(objectNode);
        }
        game.redrawBoard();
    }

    /**
     * This method interprets the "useEnvironmentCard" command.
     */
    private void interpretUseEnvironmentCard(final ActionsInput actionsInput,
                                             final Game game,
                                             final ObjectNode objectNode) {
        Card card = game.getCurrentPlayer().getCardFromHand(actionsInput.getHandIdx());
        objectNode.put("affectedRow", actionsInput.getAffectedRow());
        objectNode.put("handIdx", actionsInput.getHandIdx());
        if (card.getCardType() != CardType.ENVIRONMENT) {
            objectNode.put("error", "Chosen card is not of type environment.");
            output.add(objectNode);
            return;
        }
        ErrorType e = ((EnvironmentCard) card).tryUseAbility(game, actionsInput.getAffectedRow());
        if (e != ErrorType.NO_ERROR) {
            if (e == ErrorType.ERROR_BOARD_ROW_FULL) {
                objectNode.put("error", "Cannot steal enemy card since the player's row is full.");
                output.add(objectNode);
            } else {
                objectNode.put("error", e.interpret());
                output.add(objectNode);
            }
        } else {
            game.getCurrentPlayer().dropCardFromHand(actionsInput.getHandIdx()); // ?
        }
        game.redrawBoard();
    }

    /**
     * This method interprets the game commands.
     * @param actionsInput the given input to be parsed
     * @param game the current game
     * @return <code>true</code> if and only if the command was a game command
     */
    private boolean interpretGameAction(final ActionsInput actionsInput,
                                        final Game game) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", actionsInput.getCommand());

        switch (actionsInput.getCommand()) {
            case "endPlayerTurn":
                interpretEndPlayerTurn(game);
                break;
            case "placeCard":
                interpretPlaceCard(actionsInput, game, objectNode);
                break;
            case "cardUsesAttack":
                interpretCardUsesAttack(actionsInput, game, objectNode);
                break;
            case "cardUsesAbility":
                interpretCardUsesAbility(actionsInput, game, objectNode);
                break;
            case "useAttackHero":
                interpretUseAttackHero(actionsInput, game, objectNode);
                break;
            case "useHeroAbility":
                interpretUseHeroAbility(actionsInput, game, objectNode);
                break;
            case "useEnvironmentCard":
                interpretUseEnvironmentCard(actionsInput, game, objectNode);
                break;
            default:
                return false;
        }

        return true;
    }

    /**
     * This method runs a single game instance and stores the output.
     * @param gameInput the given input to be parsed
     */
    private void runGame(final GameInput gameInput) {
        Game game = gameInput.getStartGame().toGame(players);
        for (ActionsInput actionsInput : gameInput.getActions()) {
            if (!this.interpretDebugAction(actionsInput, game)
                    && !this.interpretGameAction(actionsInput, game)) {
                System.out.println("Uncaught action: " + actionsInput.getCommand());
            }
        }
    }

    /**
     * This method runs all the given game instances and stores the output.
     */
    public void runGames() {
        for (GameInput gameInput : gameInputs) {
            this.runGame(gameInput);
            this.currentGame++;
        }
    }

    public ArrayNode getOutput() {
        return output;
    }
}
