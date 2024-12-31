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

import hexaround.game.board.*;
import hexaround.game.creature.*;
import hexaround.game.hex.HexCoordinate;
import hexaround.game.player.*;

public class VictoryConditionsMonitor {

    private final HexBoard hexBoard;

    /**
     * Constructor for VictoryConditionsMonitor.
     * Creates a VictoryConditionsMonitor object dedicated to monitoring and determining
     * whether the victory conditions in the game are met based on the state of the butterflies
     * on the specified hex board.
     * @param hexBoard the game board used to check the placement and surroundings of the butterflies.
     */
    public VictoryConditionsMonitor(HexBoard hexBoard) {
        this.hexBoard = hexBoard;
    }

    /**
     * @param player the player whose butterfly's surrounding is to be checked.
     * @return true if a butterfly of a specified player has all its neighbors occupied on the board.
     */
    public boolean butterflyIsSurrounded(PlayerName player) {
        boolean isSurrounded = false;

        for(HexCoordinate currentCoordinate: hexBoard.getAllOccupiedHexes()) {
            if(hexBoard.getCreatureAt(currentCoordinate).getCreatureName().equals(CreatureName.BUTTERFLY) && hexBoard.getCreatureAt(currentCoordinate).getCreatureOwner().equals(player)) {
                isSurrounded = hexBoard.areAllNeighborsOccupied(currentCoordinate);
            }
        }

        return isSurrounded;
    }

    /**
     * @return true if either player's butterfly has all its neighbors occupied on the board.
     */
    public boolean eitherButterflyIsSurrounded() {
        return butterflyIsSurrounded(PlayerName.BLUE) || butterflyIsSurrounded(PlayerName.RED);
    }

    /**
     * @return true if both players' butterfly have all their neighbors occupied on the board.
     */
    public boolean bothButterfliesAreSurrounded() {
        return butterflyIsSurrounded(PlayerName.BLUE) && butterflyIsSurrounded(PlayerName.RED);
    }
}
