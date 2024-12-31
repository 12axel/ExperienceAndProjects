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

package hexaround.game.player;

import hexaround.game.player.*;
import hexaround.config.*;
import java.util.*;
public class PlayerFactory {

    /**
     * @param playerConfigs A collection of player configurations.
     * @return A map where each key is a player name (either BLUE or RED) and the value is
     * the corresponding PlayerInventory, containing the creatures allocated to that player.
     * The example code used as inspiration for this method can be found at:
     * {@link <a href="https://www.javatpoint.com/factory-method-design-pattern">...</a>}
     */
    public static Map<PlayerName, PlayerInventory> createPlayers(Collection<PlayerConfiguration> playerConfigs) {
        Map<PlayerName, PlayerInventory> players = new HashMap<>();
        for (PlayerConfiguration config : playerConfigs) {
            players.put(config.Player(), new PlayerInventory(config.creatures()));
        }

        return players;
    }
}

