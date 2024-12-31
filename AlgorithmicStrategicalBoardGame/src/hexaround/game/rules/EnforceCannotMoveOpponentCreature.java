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
import hexaround.game.hex.HexCoordinate;
import hexaround.game.player.*;

public class EnforceCannotMoveOpponentCreature {

    private final HexBoard hexBoard;

    /**
     * Constructor for the EnforceCannotMoveOpponentCreature class.
     * Creates an EnforceCannotMoveOpponentCreature object dedicated to
     * ensuring players only move their own creatures on the specified hex board.
     * @param hexBoard the current hex board with the game's creatures and their locations.
     */
    public EnforceCannotMoveOpponentCreature(HexBoard hexBoard) {
        this.hexBoard = hexBoard;
    }

    /**
     * @param playerName the name of the player.
     * @param coordinate the coordinate from which the creature's ownership is to be verified.
     * @return true if the creature at the specified coordinate belongs to the specified player.
     */
    public boolean creatureBelongsToPlayer(PlayerName playerName, HexCoordinate coordinate) {
        return hexBoard.getCreatureAt(coordinate).getCreatureOwner().equals(playerName);
    }
}
