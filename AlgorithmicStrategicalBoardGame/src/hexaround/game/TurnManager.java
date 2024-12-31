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

public class TurnManager {
    private int turnNumber;

    /**
     * Constructor for TurnManager.
     * Creates the turn manager with the turn number set to 1
     */
    public TurnManager() {
        this.turnNumber = 1;
    }

    /**
     * @return the current turn number in the game.
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * Increments the turn number by one.
     */
    public void incrementTurnNumber() {
        turnNumber++;
    }
}
