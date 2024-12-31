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

package hexaround.game.rules;

import hexaround.game.TurnManager;
import hexaround.game.board.*;
import hexaround.game.creature.*;
import hexaround.game.hex.HexCoordinate;
import hexaround.game.player.*;
public class EnsureValidCreaturePlacementBasedOnTurn {
    private final HexBoard hexBoard;
    private final TurnManager turnManager;

    /**
     * Constructor for EnsureValidCreatureBasedOnTurn.
     * Creates an EnsureValidCreatureBasedOnTurn object dedicated to
     * ensuring players place their creatures on the board in accordance with
     * all the game's creature placement rules based on the current turn number.
     * with a specified hex board and turn manager.
     * @param hexBoard the game board on which the creatures are placed.
     * @param turnManager the manager that tracks the current turn of the game.
     */
    public EnsureValidCreaturePlacementBasedOnTurn(HexBoard hexBoard, TurnManager turnManager) {
        this.hexBoard = hexBoard;
        this.turnManager = turnManager;
    }


    /**
     * @param coordinate the hex coordinate to check for neighbors with
     * creatures belonging to the specified player.
     * @param player the player whose creature's presence in neighboring hexes is to be checked.
     * @return true if the specified coordinate has at least one neighboring hex
     * with a creature belonging to the specified player.
     */
    public boolean hasNeighborWithCreatureBelongingTo(HexCoordinate coordinate, PlayerName player) {
        boolean hasCreatureBelongingToPlayer = false;

        for (HexCoordinate neighbor : coordinate.neighbors()) {
            if (hexBoard.isHexOccupied(neighbor) && hexBoard.getCreatureAt(neighbor).getCreatureOwner().equals(player)) {
                hasCreatureBelongingToPlayer = true;
            }
        }

        return hasCreatureBelongingToPlayer;
    }

    /**
     * @param coordinate the hex coordinate to check for neighbors with
     * creatures not belonging to the specified player.
     * @param player the player whose creature's absence in neighboring hexes is to be checked.
     * @return true if the specified coordinate has at least one neighboring hex
     * with a creature not belonging to the specified player.
     */
    public boolean hasNeighborWithCreatureNotBelongingTo(HexCoordinate coordinate, PlayerName player) {
        boolean hasCreatureNotBelongingToPlayer = false;
        for (HexCoordinate neighbor : coordinate.neighbors()) {
            if (hexBoard.isHexOccupied(neighbor) && !hexBoard.getCreatureAt(neighbor).getCreatureOwner().equals(player)) {
                hasCreatureNotBelongingToPlayer = true;
            }
        }

        return hasCreatureNotBelongingToPlayer;
    }

    /**
     * @param coordinate the hex coordinate on the game board where a specified player
     * wants to place a creature.
     * @param player the player attempting to place the creature on the specified hex coordinate
     * on the game board.
     * @return true if the placement is valid based on the in accordance with
     * all of the game's creature placement rules based on the current turn number.
     */
    public boolean isValidCreaturePlacement(HexCoordinate coordinate, PlayerName player) {
        boolean isValid = false;

        if (turnManager.getTurnNumber() == 1) {
            isValid = coordinate.equals(new HexCoordinate(0, 0));
        }
        else if (turnManager.getTurnNumber() == 2) {
            for(HexCoordinate neighbor : coordinate.neighbors()) {
                if(hexBoard.isHexOccupied(neighbor)){
                    isValid = true;
                }
            }
        }
        else {
            isValid = hasNeighborWithCreatureBelongingTo(coordinate, player) && !hasNeighborWithCreatureNotBelongingTo(coordinate, player);
        }

        return isValid;
    }
}
