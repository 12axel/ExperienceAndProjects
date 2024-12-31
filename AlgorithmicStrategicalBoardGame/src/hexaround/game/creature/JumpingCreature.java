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

package hexaround.game.creature;

import hexaround.config.CreatureDefinition;
import hexaround.game.board.HexBoard;
import hexaround.game.hex.HexCoordinate;
import hexaround.game.moves.MovementStrategy;
import hexaround.game.rules.EnsureConnectedness;

public class JumpingCreature extends Creature{

    public JumpingCreature(CreatureDefinition definition, MovementStrategy movementStrategy){
        super(definition, movementStrategy);
    }

    @Override
    public boolean canExecuteRequestedMove(HexCoordinate fromCoordinate, HexCoordinate toCoordinate, HexBoard hexBoard, EnsureConnectedness ensureConnectedness) {
        return super.canExecuteRequestedMove(fromCoordinate, toCoordinate, hexBoard, ensureConnectedness);
    }
}
