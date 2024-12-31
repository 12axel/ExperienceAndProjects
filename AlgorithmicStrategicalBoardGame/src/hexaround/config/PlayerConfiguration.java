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
 *  This record:
 *  MAY NOT be MODIFIED, except that you can modify toString()
 *  MAY NOT be MOVED to a different package.
 */

package hexaround.config;

import hexaround.game.player.*;
import hexaround.game.creature.*;

import java.util.*;

/**
 * This record contains the information for a single player. It has
 * all of the creatures that the player has at the beginning of the
 * game.
 * @param Player RED or BLUE
 * @param creatures
 */
public record PlayerConfiguration(PlayerName Player, Map<CreatureName, Integer> creatures) {
    @Override
    public String toString() {
        return "\tPlayerConfiguration{" +
            "Player=" + Player +
            ",\n\t\t\tcreatures=" + creatures +
            "}\n";
    }
}
