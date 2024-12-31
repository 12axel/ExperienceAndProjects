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
import hexaround.game.rules.*;

public class JumpingMovementStrategy implements MovementStrategy {

    @Override
    public boolean canExecuteRequestedMove(HexCoordinate from, HexCoordinate to, int maxDistance, HexBoard hexBoard, EnsureConnectedness ensureConnectedness) {
        return (from.distanceTo(to) <= maxDistance
                && from.isLinear(to)
                && ensureConnectedness.moveMaintainsConnectivity(from, to)
        );
    }
}
