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

package hexaround.game.moves;

import hexaround.game.board.*;
import hexaround.game.creature.*;
import hexaround.game.hex.HexCoordinate;
import hexaround.game.rules.EnsureConnectedness;

public interface MovementStrategy {

    /**
     * The following resource was used to help me with the implementation of this interface
     * and the classes that implement it:
     * {@link https://refactoring.guru/design-patterns/strategy/java/example}
     */

    /**
     * @param from The starting hex coordinate of the creature.
     * @param to The target hex coordinate where the creature intends to move.
     * @param creatureMaxDistance The maximum distance a creature is allowed to move.
     * @param hexBoard The current state of the game board.
     * @param ensureConnectedness The object to ensure that the requested move follows
     * all the game's creature connectedness rules.
     * @return true if the requested move is legal and can be executed according
     * to the game rules and the creature's abilities.
     */
    boolean canExecuteRequestedMove(HexCoordinate from, HexCoordinate to, int creatureMaxDistance, HexBoard hexBoard, EnsureConnectedness ensureConnectedness);
}
