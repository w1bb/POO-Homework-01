package execution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import execution.cards.Card;
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

        if (actionsInput.getCommand().equals("getCardsInHand")) {
            objectNode.put("playerIdx", actionsInput.getPlayerIdx());
            objectNode.set("output", players[actionsInput.getPlayerIdx() - 1].currentHandToArrayNode(objectMapper));
        } else if (actionsInput.getCommand().equals("getPlayerDeck")) {
            objectNode.put("playerIdx", actionsInput.getPlayerIdx());
            objectNode.set("output", players[actionsInput.getPlayerIdx() - 1].getCurrentDeck().toArrayNode(objectMapper));
        } else if (actionsInput.getCommand().equals("getCardsOnTable")) {
            objectNode.set("output", game.boardToArrayNode(objectMapper));
        } else if (actionsInput.getCommand().equals("getPlayerTurn")) {
            objectNode.put("output", (game.getCurrentPlayer() == players[0]) ? 1 : 2);
        } else if (actionsInput.getCommand().equals("getPlayerHero")) {
            objectNode.put("playerIdx", actionsInput.getPlayerIdx());
            objectNode.set("output", players[actionsInput.getPlayerIdx() - 1].getHeroCard().toObjectNode(objectMapper));
        } else if (actionsInput.getCommand().equals("getCardAtPosition")) {
            objectNode.put("x", actionsInput.getX());
            objectNode.put("y", actionsInput.getY());
            MinionCard card = game.getCard(actionsInput.getY(), actionsInput.getX());
            if (card == null)
                objectNode.put("output", "No card at that position.");
            else
                objectNode.set("output", card.toObjectNode(objectMapper));
        } else if (actionsInput.getCommand().equals("getPlayerMana")) {
            objectNode.put("playerIdx", actionsInput.getPlayerIdx());
            objectNode.put("output", players[actionsInput.getPlayerIdx() - 1].getMana());
        } else if (actionsInput.getCommand().equals("getEnvironmentCardsInHand")) {
            objectNode.put("playerIdx", actionsInput.getPlayerIdx());
            objectNode.set("output", players[actionsInput.getPlayerIdx() - 1].currentHandEnvironmentToArrayNode(objectMapper));
        } else if (actionsInput.getCommand().equals("getFrozenCardsOnTable")) {
            objectNode.set("output", game.boardFrozenToArrayNode(objectMapper));
        } else if (actionsInput.getCommand().equals("getTotalGamesPlayed")) {
            objectNode.put("output", this.currentGame);
        } else if (actionsInput.getCommand().equals("getPlayerOneWins")) {
            objectNode.put("output", players[0].getWins());
        } else if (actionsInput.getCommand().equals("getPlayerTwoWins")) {
            objectNode.put("output", players[1].getWins());
        } else {
            return false;
        }
        output.add(objectNode);
        return true;
    }

    private Boolean interpretGameAction(ActionsInput actionsInput, Game game) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", actionsInput.getCommand());

        if (actionsInput.getCommand().equals("endPlayerTurn")) {
            game.endTurn();
            return true;
        } else if (actionsInput.getCommand().equals("placeCard")) {
            Card card = game.getCurrentPlayer().getCardFromHand(actionsInput.getHandIdx());
            if (card.getCardType() == 2)
                objectNode.put("error", "Cannot place environment card on table.");
            else {
                game.getCurrentPlayer().dropCardFromHand(actionsInput.getHandIdx()); // ?
                game.addCardOnBoard(card, actionsInput.getY(), actionsInput.getX());
                game.redrawBoard();
            }
            return true;
        }
        return false;
    }

    private void runGame(int gameIdx) {
        Game game = gameInputs.get(gameIdx).getStartGame().getGame(players);
        for (ActionsInput actionsInput : gameInputs.get(gameIdx).getActions()) {
            if (this.interpretDebugAction(actionsInput, game) || this.interpretGameAction(actionsInput, game)) {

            }
        }
    }

    public void runGames() {
        for (int gameIdx = 0; gameIdx < gameInputs.size(); ++gameIdx) {
            this.runGame(gameIdx);
            this.currentGame++;
        }
    }
}
