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

import hexaround.game.creature.CreatureName;

/**
 * This interface describes the behavior of HexAround from the client's viewpoint.
 * The client can be any test of the game's functionality or an implementation of
 * the full game that uses this interface for its game management.
 * <p>
 * You may extend this interface internally, but this is the public interface that
 * all clients will use. It is the only way that a client can interact
 * with the implementation of the game manager.
 *
 * @version May 30, 2020
 * <p>
 * This class:
 *  MAY NOT be MODIFIED
 *  MAY NOT be MOVED to a different package.
 */
public interface IHexAroundGameManager {
    GameState placeCreature(CreatureName creature, int x, int y);
    GameState moveCreature(CreatureName creature, int fromX, int fromY, int toX, int toY);
}
