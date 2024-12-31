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

import hexaround.config.*;
import hexaround.game.creature.*;
import hexaround.game.player.*;
import java.util.*;
import java.io.*;

public class HexAroundGameBuilder {

    private Map<CreatureName, Creature> creatures;
    private Map<PlayerName, PlayerInventory> players;

    /**
     * The following resource was used to help me with the implementation of this class:
     * {@link https://refactoring.guru/design-patterns/builder/java/example}
     */

    /**
     * Sets the creature definitions for the game builder.
     * @param creatures a map of creature names to their respective creature objects.
     */
    public void setCreatures(Map<CreatureName, Creature> creatures) {
        this.creatures = creatures;
    }

    /**
     * Sets the player inventories for the game.
     * @param players a map of player names to their respective player inventories.
     */
    public void setPlayers(Map<PlayerName, PlayerInventory> players) {
        this.players = players;
    }

    /**
     * @return A new instance of IHexAroundGameManager based on the set configurations.
     */
    public IHexAroundGameManager build() {
        return new HexAroundGameManager(creatures, players);
    }

    /**
     * @param configurationFile The file path of the game configuration file.
     * @return a new instance of IHexAroundGameManager based on the configuration file.
     * @throws IOException if there is an error reading the configuration file.
     */
    public static IHexAroundGameManager buildGameManager(String configurationFile) throws IOException {
        HexAroundConfigurationMaker configurationMaker = new HexAroundConfigurationMaker(configurationFile);
        GameConfiguration config = configurationMaker.makeConfiguration();
        HexAroundGameBuilder builder = new HexAroundGameBuilder();
        builder.setCreatures(CreatureFactory.createCreatures(config.creatures()));
        builder.setPlayers(PlayerFactory.createPlayers(config.players()));
        return builder.build();
    }
}
