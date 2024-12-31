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

package hexaround.game.board;

import hexaround.game.creature.*;
import hexaround.game.player.*;
import java.util.*;

public class PlacedCreature {
    private final CreatureName creatureName;
    private final PlayerName creatureOwner;

    /**
     * Constructor for PlacedCreature.
     * Creates a PlacedCreature with a specified creature name and player.
     * @param creatureName the name of the creature.
     * @param creatureOwner the player who owns the creature.
     */
    public PlacedCreature(CreatureName creatureName, PlayerName creatureOwner) {
        this.creatureName = creatureName;
        this.creatureOwner = creatureOwner;
    }

    /**
     * @return the name of the creature.
     */
    public CreatureName getCreatureName() {
        return creatureName;
    }

    /**
     * @return the owner of the creature (the player who placed it on the board).
     */
    public PlayerName getCreatureOwner() {
        return creatureOwner;
    }
}
