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
import java.util.function.*;
import java.util.*;

public class MoveValidator {

    private final HexBoard hexBoard;
    private final Map<PlayerName, PlayerInventory> players;
    private final Map<CreatureName, Creature> creatures;
    private final VictoryConditionsMonitor victoryConditionsMonitor;
    private final EnsureValidCreaturePlacementBasedOnTurn ensureValidCreaturePlacementBasedOnTurn;
    private final EnsureTimelyButterflyPlacement ensureTimelyButterflyPlacement;
    private final EnforceCannotMoveOpponentCreature enforceCannotMoveOpponentCreature;
    private final EnsureConnectedness ensureConnectedness;

    /**
     * Constructor for MoveValidator.
     * Creates a MoveValidator object, using the specified hexboard, playerInventories, creatureDefinitions,
     * turnManager, and victoryConditionsMonitor, that is dedicated to making sure that a move a player
     * declares that they want to make is compliant with all the game's rules before letting them execute
     * it.
     * @param hexboard the board on which the game is played.
     * @param players a map of player names to their respective player inventories.
     * @param creatures a map of creature names to their respective creature objects.
     * @param turnManager the manager that tracks the current turn of the game.
     * @param victoryConditionsMonitor an object dedicated to monitoring and determining
     * whether the victory conditions in the game are met based on the state of the butterflies
     * on the game board.
     */
    public MoveValidator(HexBoard hexboard, Map<PlayerName, PlayerInventory> players, Map<CreatureName, Creature> creatures, TurnManager turnManager, VictoryConditionsMonitor victoryConditionsMonitor) {
        this.hexBoard = hexboard;
        this.players = players;
        this.creatures = creatures;
        this.victoryConditionsMonitor = victoryConditionsMonitor;
        this.ensureValidCreaturePlacementBasedOnTurn = new EnsureValidCreaturePlacementBasedOnTurn(hexBoard, turnManager);
        this.ensureTimelyButterflyPlacement = new EnsureTimelyButterflyPlacement(hexBoard, turnManager);
        this.enforceCannotMoveOpponentCreature = new EnforceCannotMoveOpponentCreature(hexBoard);
        this.ensureConnectedness = new EnsureConnectedness(hexBoard);
    }

    /**
     * For the next 2 methods, the example code used as inspiration
     * for this method can be found at:
     * Office hour (13-Dec-2023)
     * {@link <a href="https://www.geeksforgeeks.org/supplier-interface-in-java-with-examples/">...</a>}
     */

    /**
     * @param player the name of the player wishing to place a creature on the game board.
     * @param creature the name of the creature the specified player wishes to place on the game board.
     * @param coordinate the coordinate on the game board where the specified player wishes to place the
     * specified creature.
     * @return a GameState based on whether the specified player is allowed to place the specified creature
     * on the specified coordinate on the game board based on the game's creature placement rules.
     */
    public GameState validateCreaturePlacement(PlayerName player, CreatureName creature, HexCoordinate coordinate) {
        GameState state = new GameState(MoveResult.OK);

        List<Supplier<Boolean>> errorChecks = Arrays.asList(
                () -> !players.get(player).containsCreature(creature),
                () -> hexBoard.isHexOccupied(coordinate),
                () -> !ensureValidCreaturePlacementBasedOnTurn.isValidCreaturePlacement(coordinate, player),
                () -> !ensureTimelyButterflyPlacement.butterfliesHaveBeenPlacedTimely() && !ensureTimelyButterflyPlacement.butterflyIsBeingPlacedThisMove(creature),
                () -> victoryConditionsMonitor.eitherButterflyIsSurrounded()
        );

        List<String> errorMessages = Arrays.asList(
                "Creature not in player's inventory",
                "Hex is already occupied.",
                "Creature Placement Turn Rules have been broken.",
                "The butterfly has not been placed timely.",
                "A butterfly is surrounded, the game is over."
        );

        for (int i = 0; i < errorChecks.size(); i++) {
            if (state.moveResult().equals(MoveResult.OK) && errorChecks.get(i).get()) {
                state = new GameState(MoveResult.MOVE_ERROR, errorMessages.get(i));
            }
        }

        return state;
    }

    /**
     * @param player the name of the player wishing to move a creature from one coordinate to another
     * on the game board.
     * @param creature the name of the creature the specified player wishes to move on from one coordinate
     * to another on the game board.
     * @param fromCoordinate the coordinate on the game board where the specified player wishes move the
     * specified creature from.
     * @param toCoordinate the coordinate on the game board where the specified player wishes move the
     * specified creature to.
     * @return a GameState based on whether the specified player is allowed to move the specified creature
     * from the specified coordinate on the game board they wish to move it from to the specified coordinate
     * on the game board they wish to move it to based on the game's creature movement rules.
     */
    public GameState validateCreatureMovement(PlayerName player, CreatureName creature, HexCoordinate fromCoordinate, HexCoordinate toCoordinate) {
        GameState state = new GameState(MoveResult.OK);

        List<Supplier<Boolean>> errorChecks = Arrays.asList(
                () -> fromCoordinate.equals(toCoordinate),
                () -> !hexBoard.isHexOccupied(fromCoordinate),
                () -> !hexBoard.getCreatureAt(fromCoordinate).getCreatureName().equals(creature),
                () -> hexBoard.isHexOccupied(toCoordinate),
                () -> !enforceCannotMoveOpponentCreature.creatureBelongsToPlayer(player, fromCoordinate),
                () -> !ensureTimelyButterflyPlacement.butterfliesHaveBeenPlacedTimely(),
                () -> victoryConditionsMonitor.eitherButterflyIsSurrounded(),
                () -> !creatures.get(creature).canExecuteRequestedMove(fromCoordinate, toCoordinate, hexBoard, ensureConnectedness)
        );

        List<String> errorMessages = Arrays.asList(
                "The 'from' hex is the same as the 'to' hex.",
                "There is no piece on the 'from' hex.",
                "The specified creature is not on the 'from' hex.",
                "The 'to' hex already has a creature on it.",
                "Cannot move an opponent's creature.",
                "The butterfly has not been placed timely.",
                "A butterfly is surrounded, the game is over.",
                "This move is not valid for the creature's movement rules."
        );

        for (int i = 0; i < errorChecks.size(); i++) {
            if(state.moveResult().equals(MoveResult.OK) && errorChecks.get(i).get()) {
                state = new GameState(MoveResult.MOVE_ERROR, errorMessages.get(i));
            }
        }

        return state;
    }
}
