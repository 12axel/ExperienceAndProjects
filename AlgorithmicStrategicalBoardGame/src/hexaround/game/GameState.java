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

/**
 * This is what is returned from making a move in a game.
 * @param moveResult
 * @param message
 */
public record GameState(
    MoveResult moveResult,
    String message  // The message must be filled in if there is any error
){
    /**
     * Shortcut that calls the default constructor with a null message.
     * @param moveResult
     */
    public GameState(MoveResult moveResult) {
        this(moveResult, null);
    }
}
