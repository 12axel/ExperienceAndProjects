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

import hexaround.game.player.*;
public class PlayerManager {

    private PlayerName currentPlayer;

    /**
     * Constructor for PlayerManager.
     * Creates a PlayerManager with the first player set to BLUE.
     */
    public PlayerManager() {
        this.currentPlayer = PlayerName.BLUE;
    }

    /**
     * @return the player name representing the current player in the game.
     */
    public PlayerName getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player in the game.
     * @param playerName the player name to set as the current player in the game.
     */
    public void setCurrentPlayer(PlayerName playerName) {
        this.currentPlayer = playerName;
    }

    /**
     * Switches the current player to the next player.
     */
    public void switchToNextPlayer() {
        this.currentPlayer = (this.currentPlayer.equals(PlayerName.BLUE)) ? PlayerName.RED : PlayerName.BLUE;
    }
}


