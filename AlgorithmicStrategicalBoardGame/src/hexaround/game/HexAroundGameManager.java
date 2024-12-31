/*
 * ******************************************************************************
 *  This files was developed for CS4233: Object-Oriented Analysis & Design.
 *  The course was taken at Worcester Polytechnic Institute.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  * Copyright ©2016-2017 Gary F. Pollice
 *  ******************************************************************************
 *
 */

package hexaround.game;
import hexaround.game.hex.HexCoordinate;
import hexaround.game.rules.*;
import hexaround.game.creature.*;
import hexaround.game.player.*;
import hexaround.game.board.*;
import hexaround.game.player.PlayerName;
import java.util.*;

public class HexAroundGameManager implements IHexAroundGameManager {

    private final HexBoard hexBoard;
    private final PlayerManager playerManager;
    private final Map<PlayerName, PlayerInventory> players;
    private final VictoryConditionsMonitor victoryConditionsMonitor;
    private final TurnManager turnManager;
    private final MoveValidator moveValidator;
    private final GameOutcomeEvaluator gameOutcomeEvaluator;

    /**
     * Constructor for HexAroundGameManager.
     * Creates a HexAroundGameManager object, using the specified creatureDefinitions and
     * playerInventories, that is dedicated to managing the overall flow of the game as it
     * is being played.
     * @param creatures a map of creature names to their respective creature objects.
     * @param players a map of player names to their respective player inventories.
     */
    public HexAroundGameManager(Map<CreatureName, Creature> creatures, Map<PlayerName, PlayerInventory> players) {
        this.hexBoard = new HexBoard();
        this.playerManager = new PlayerManager();
        this.turnManager = new TurnManager();
        this.players = players;
        this.victoryConditionsMonitor = new VictoryConditionsMonitor(hexBoard);
        this.moveValidator = new MoveValidator(hexBoard, players, creatures, turnManager, victoryConditionsMonitor);
        this.gameOutcomeEvaluator = new GameOutcomeEvaluator(victoryConditionsMonitor);
    }

    /**
     * @return the hex board representing the current state of the game's board.
     */
    public HexBoard getHexBoard() {
        return hexBoard;
    }

    /**
     * @return the turn manager that is responsible for managing the turns in the current game.
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }

    /**
     * @return the player manager that is responsible for managing the players in the current game.
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public GameState placeCreature(CreatureName creature, int x, int y) {
        HexCoordinate coordinate = new HexCoordinate(x, y);
        PlayerName currentPlayer = playerManager.getCurrentPlayer();
        GameState state = moveValidator.validateCreaturePlacement(currentPlayer, creature, coordinate);

        if (state.moveResult().equals(MoveResult.OK)) {
            hexBoard.placeCreature(creature, coordinate, currentPlayer);
            players.get(currentPlayer).removeCreature(creature);
            playerManager.switchToNextPlayer();
            turnManager.incrementTurnNumber();
            state = gameOutcomeEvaluator.evaluateGameOutcome();
        }

        return state;
    }

    @Override
    public GameState moveCreature(CreatureName creature, int fromX, int fromY, int toX, int toY) {
        HexCoordinate fromCoordinate = new HexCoordinate(fromX, fromY);
        HexCoordinate toCoordinate = new HexCoordinate(toX, toY);
        PlayerName currentPlayer = playerManager.getCurrentPlayer();
        GameState state = moveValidator.validateCreatureMovement(currentPlayer, creature, fromCoordinate, toCoordinate);

        if (state.moveResult().equals(MoveResult.OK)) {
            hexBoard.removeCreature(fromCoordinate);
            hexBoard.placeCreature(creature, toCoordinate, currentPlayer);
            playerManager.switchToNextPlayer();
            turnManager.incrementTurnNumber();
            state = gameOutcomeEvaluator.evaluateGameOutcome();
        }

        return state;
    }
}
