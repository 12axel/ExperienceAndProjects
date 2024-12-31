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
import java.util.*;
public class EnsureTimelyButterflyPlacement {

    private final HexBoard hexBoard;
    private final TurnManager turnManager;

    /**
     * Constructor for EnsureTimelyButterflyPlacement.
     * Creates an EnsureTimelyButterflyPlacement object dedicated to
     * ensuring players place their butterfly on the board by the time they are supposed to
     * with a specified hex board and turn manager.
     * @param hexBoard the game board on which the creatures are placed.
     * @param turnManager the manager that tracks the current turn of the game.
     */
    public EnsureTimelyButterflyPlacement(HexBoard hexBoard, TurnManager turnManager) {
        this.hexBoard = hexBoard;
        this.turnManager = turnManager;
    }

    /**
     * @param creature the name of the creature.
     * @return true if the specified creature is a butterfly being placed on the game board
     * during the current move.
     */
    public boolean butterflyIsBeingPlacedThisMove(CreatureName creature) {
        return creature.equals(CreatureName.BUTTERFLY);
    }

    /**
     * @param playerName the name of the player.
     * @return true if the specified player's butterfly is placed on the board.
     */
    public boolean hasPlacedButterfly(PlayerName playerName) {
        boolean isPlaced = false;

        for (HexCoordinate occupiedCoordinate : hexBoard.getAllOccupiedHexes()) {
            if (hexBoard.getCreatureAt(occupiedCoordinate).getCreatureName().equals(CreatureName.BUTTERFLY)
                    && hexBoard.getCreatureAt(occupiedCoordinate).getCreatureOwner().equals(playerName)) {
                isPlaced = true;
            }
        }

        return isPlaced;
    }

    /**
     * @return true if both players placed their butterflies on the board
     * by the end of their respective 4th turn (the blue player needs to place
     * their butterfly on the board by the end of turn 7, and the red player by
     * the end of turn 8 as 2 individual player turns equals 1 full turn).
     */
    public boolean butterfliesHaveBeenPlacedTimely() {
        boolean placedTimely = true;

        if (turnManager.getTurnNumber() >= 7) {
            boolean blueButterflyPlaced = hasPlacedButterfly(PlayerName.BLUE);
            placedTimely = blueButterflyPlaced;

            if (turnManager.getTurnNumber() >= 8) {
                boolean redButterflyPlaced = hasPlacedButterfly(PlayerName.RED);
                placedTimely = blueButterflyPlaced && redButterflyPlaced;
            }
        }

        return placedTimely;
    }
}
