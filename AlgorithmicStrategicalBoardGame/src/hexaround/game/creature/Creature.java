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

public abstract class Creature {

    /**
     * The following sources was used for the implementation of this abstract class and the
     * classes that extend it:
     * {@link <a href="https://refactoring.guru/design-patterns/strategy/java/example">...</a>}
     * {@link <a href="https://www.geeksforgeeks.org/strategy-pattern-set-1/">...</a>}
     * {@link <a href="https://medium.com/@akshatsharma0610/strategy-design-pattern-in-java-6ee96f87d807">...</a>}
     */

    private final CreatureDefinition definition;
    private final MovementStrategy movementStrategy;

    /**
     * Constructor for Creature.
     * Creates a creature with a specified movement Strategy.
     * @param definition the creature's associated creature definition.
     * @param movementStrategy the creature's associated movement strategy.
     */
    public Creature(CreatureDefinition definition, MovementStrategy movementStrategy) {
        this.definition = definition;
        this.movementStrategy = movementStrategy;
    }

    /**
     * @param fromCoordinate The starting hex coordinate of the creature.
     * @param toCoordinate The target hex coordinate where the creature intends to move.
     * @param hexBoard The current state of the game board.
     * @param ensureConnectedness The object to ensure that the requested move follows
     * all the game's creature connectedness rules.
     * @return true if the requested move is legal and can be executed according to the
     * game rules and the creature's abilities.
     */
    public boolean canExecuteRequestedMove(HexCoordinate fromCoordinate, HexCoordinate toCoordinate, HexBoard hexBoard, EnsureConnectedness ensureConnectedness){
        return movementStrategy.canExecuteRequestedMove(fromCoordinate, toCoordinate, definition.maxDistance(), hexBoard, ensureConnectedness);
    }
}
