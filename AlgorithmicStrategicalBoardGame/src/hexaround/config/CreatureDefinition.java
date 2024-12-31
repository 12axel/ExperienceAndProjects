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
 * This record:
 *  MAY NOT be MODIFIED, except that you can modify toString()
 *  MAY NOT be MOVED to a different package.
 */

package hexaround.config;

import hexaround.game.creature.*;

import java.util.*;

/**
 * @param name the name of the creature
 * @param maxDistance the maximum number of hexes that it can move in one turn
 * @param properties the properties that govern the creature's behavior.
 */
public record CreatureDefinition(CreatureName name, int maxDistance,
                                 Collection<CreatureProperty> properties) {
    @Override
    public String toString() {
        return "\tCreatureDefinition{" +
            "name=" + name +
            ", maxDistance=" + maxDistance +
            ", \n\t\tproperties=" + properties +
            "}\n";
    }
}
