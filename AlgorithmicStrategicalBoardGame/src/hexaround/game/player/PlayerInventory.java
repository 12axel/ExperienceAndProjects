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
import hexaround.game.creature.*;
import hexaround.game.creature.CreatureName;

import java.util.*;

public class PlayerInventory {
    private final Map<CreatureName, CreatureCounter> inventory;

    /**
     * Constructor for PlayerInventory.
     * Creates a player's inventory with a specified map of creature names and their count values.
     * @param initialInventory the initial set of creatures and their counts for the inventory.
     */
    public PlayerInventory(Map<CreatureName, Integer> initialInventory) {
        this.inventory = new HashMap<>();
        for (Map.Entry<CreatureName, Integer> entry : initialInventory.entrySet()) {
            this.inventory.put(entry.getKey(), new CreatureCounter(entry.getValue()));
        }
    }

    /**
     * @param creature the creature name to check in the inventory.
     * @return true if the inventory has one or more units of the specified creature.
     */
    public boolean containsCreature(CreatureName creature) {
        boolean containsCreature = false;
        if (inventory.get(creature) != null) {
            containsCreature = inventory.get(creature).getCount() > 0;
        }
        return containsCreature;
    }

    /**
     * Removes one unit of the specified creature from the inventory.
     * @param creature The creature name to be removed from the inventory.
     */
    public void removeCreature(CreatureName creature) {
        inventory.get(creature).decrementCount();
    }
}


