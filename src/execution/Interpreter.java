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

public class Interpreter {
    Player[] players;
    ArrayList<GameInput> gameInputs;
    ObjectMapper objectMapper;

    ArrayNode output;

    int currentGame;

    public Interpreter(Input inputData, ObjectMapper objectMapper) {
        players = new Player[2];
        players[0] = new Player(0, inputData.getPlayerOneDecks().getArrayOfDeck(0));
        players[1] = new Player(1, inputData.getPlayerTwoDecks().getArrayOfDeck(1));
        this.gameInputs = inputData.getGames();
        this.objectMapper = objectMapper;
        this.currentGame = 0;
        this.output = objectMapper.createArrayNode();
    }

    private Boolean interpretDebugAction(ActionsInput actionsInput, Game game) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", actionsInput.getCommand());

        switch (actionsInput.getCommand()) {
            case "getCardsInHand":
                objectNode.put("playerIdx", actionsInput.getPlayerIdx());
                objectNode.set("output", players[actionsInput.getPlayerIdx() - 1].currentHandToArrayNode(objectMapper));
                break;
            case "getPlayerDeck":
                objectNode.put("playerIdx", actionsInput.getPlayerIdx());
                objectNode.set("output", players[actionsInput.getPlayerIdx() - 1].getCurrentDeck().toArrayNode(objectMapper));
                break;
            case "getCardsOnTable":
                objectNode.set("output", game.boardToArrayNode(objectMapper));
                break;
            case "getPlayerTurn":
                objectNode.put("output", (game.getCurrentPlayer() == players[0]) ? 1 : 2);
                break;
            case "getPlayerHero":
                objectNode.put("playerIdx", actionsInput.getPlayerIdx());
                objectNode.set("output", players[actionsInput.getPlayerIdx() - 1].getHeroCard().toObjectNode(objectMapper));
                break;
            case "getCardAtPosition":
                objectNode.put("x", actionsInput.getX());
                objectNode.put("y", actionsInput.getY());
                MinionCard card = game.getCard(actionsInput.getY(), actionsInput.getX());
                if (card == null)
                    objectNode.put("output", "No card at that position.");
                else
                    objectNode.set("output", card.toObjectNode(objectMapper));
                break;
            case "getPlayerMana":
                objectNode.put("playerIdx", actionsInput.getPlayerIdx());
                objectNode.put("output", players[actionsInput.getPlayerIdx() - 1].getMana());
                break;
            case "getEnvironmentCardsInHand":
                objectNode.put("playerIdx", actionsInput.getPlayerIdx());
                objectNode.set("output", players[actionsInput.getPlayerIdx() - 1].currentHandEnvironmentToArrayNode(objectMapper));
                break;
            case "getFrozenCardsOnTable":
                objectNode.set("output", game.boardFrozenToArrayNode(objectMapper));
                break;
            case "getTotalGamesPlayed":
                objectNode.put("output", this.currentGame);
                break;
            case "getPlayerOneWins":
                objectNode.put("output", players[0].getWins());
                break;
            case "getPlayerTwoWins":
                objectNode.put("output", players[1].getWins());
                break;
            default:
                return false;
        }
        output.add(objectNode);
        return true;
    }

    private Boolean interpretGameAction(ActionsInput actionsInput, Game game) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", actionsInput.getCommand());
        ErrorType e;

        switch (actionsInput.getCommand()) {
            case "endPlayerTurn" -> {
                game.endTurn();
                return true;
            }
            case "placeCard" -> {
                e = game.getCurrentPlayer().placeCardOnBoard(actionsInput.getHandIdx(), game);
                if (e != ErrorType.NO_ERROR) {
                    objectNode.put("error", e.interpret());
                    output.add(objectNode);
                }
                game.redrawBoard();
                return true;
            }
            case "cardUsesAttack" -> {
                int attackerY = actionsInput.getCardAttacker().getY();
                int attackerX = actionsInput.getCardAttacker().getX();
                MinionCard attackerCard = game.getCard(attackerY, attackerX);
                int attackedY = actionsInput.getCardAttacked().getY();
                int attackedX = actionsInput.getCardAttacked().getX();
                Card attackedCard = game.getCard(attackedY, attackedX);
                e = attackerCard.tryUseAttack(game, attackedCard);
                if (e != ErrorType.NO_ERROR) {
                    objectNode.put("error", e.interpret());
                    output.add(objectNode);
                }
                game.redrawBoard();
                return true;
            }
            case "cardUsesAbility" -> {
                int attackerY = actionsInput.getCardAttacker().getY();
                int attackerX = actionsInput.getCardAttacker().getX();
                MinionCard attackerCard = game.getCard(attackerY, attackerX);
                int attackedY = actionsInput.getCardAttacked().getY();
                int attackedX = actionsInput.getCardAttacked().getX();
                MinionCard attackedCard = game.getCard(attackedY, attackedX);
                e = attackerCard.tryUseAbility(game, attackedCard);
                if (e != ErrorType.NO_ERROR) {
                    objectNode.put("error", e.interpret());
                    output.add(objectNode);
                }
                game.redrawBoard();
                return true;
            }
            case "useAttackHero" -> {
                int attackerY = actionsInput.getCardAttacker().getY();
                int attackerX = actionsInput.getCardAttacker().getX();
                MinionCard attackerCard = game.getCard(attackerY, attackerX);
                HeroCard attackedCard = game.getNextPlayer().getHeroCard();
                e = attackerCard.tryUseAttack(game, attackedCard);
                if (e != ErrorType.NO_ERROR) {
                    objectNode.put("error", e.interpret());
                    output.add(objectNode);
                }
                else if (game.isOver()) {
                    if (players[0].getHeroCard().getHealth() > 0) {
                        objectNode.put("gameEnded", "layer one killed the enemy hero.");
                        output.add(objectNode);
                    }
                    else {
                        objectNode.put("gameEnded", "layer two killed the enemy hero.");
                        output.add(objectNode);
                    }
                }
                game.redrawBoard();
                return true;
            }
            case "useHeroAbility" -> {
                int row = actionsInput.getAffectedRow();
                HeroCard attackerCard = game.getCurrentPlayer().getHeroCard();
                e = attackerCard.tryUseAbility(game, row, game.getCurrentPlayer());
                if (e != ErrorType.NO_ERROR) {
                    objectNode.put("error", e.interpret());
                    output.add(objectNode);
                }
                game.redrawBoard();
                return true;
            }
            case "useEnvironmentCard" -> {
                Card card = game.getCurrentPlayer().getCardFromHand(actionsInput.getHandIdx());
                if (card.getCardType() != 2) {
                    objectNode.put("error", "Chosen card is not of type environment.");
                    output.add(objectNode);
                    return true;
                }
                e = ((EnvironmentCard)card).tryUseAbility(game, actionsInput.getAffectedRow());
                if (e != ErrorType.NO_ERROR) {
                    if (e == ErrorType.ERROR_BOARD_ROW_FULL) {
                        objectNode.put("error", "Cannot steal enemy card since the player's row is full.");
                        output.add(objectNode);
                    }
                    else {
                        objectNode.put("error", e.interpret());
                        output.add(objectNode);
                    }
                } else {
                    game.getCurrentPlayer().dropCardFromHand(actionsInput.getHandIdx()); // ?
                }
                game.redrawBoard();
                return true;
            }
        }
        return false;
    }

    private void runGame(int gameIdx) {
        Game game = gameInputs.get(gameIdx).getStartGame().getGame(players);
        for (ActionsInput actionsInput : gameInputs.get(gameIdx).getActions()) {
            if (!this.interpretDebugAction(actionsInput, game) && !this.interpretGameAction(actionsInput, game)) {
                System.out.println("Uncaught action: " + actionsInput.getCommand());
            }
        }
    }

    public void runGames() {
        for (int gameIdx = 0; gameIdx < gameInputs.size(); ++gameIdx) {
            this.runGame(gameIdx);
            this.currentGame++;
        }
    }

    public ArrayNode getOutput() {
        return output;
    }
}
