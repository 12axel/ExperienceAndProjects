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

import java.util.*;

/**
 * This record contains all of the information needed to build a game for the
 * game manager.
 * @param creatures
 * @param players
 */
public record GameConfiguration(Collection<CreatureDefinition> creatures,
    Collection<PlayerConfiguration> players) {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATURES:\n");
        for (CreatureDefinition cd : creatures) {
            builder.append(cd.toString());
        }
        builder.append("PLAYERS:\n");
        for (PlayerConfiguration pc : players) {
            builder.append(pc.toString());
        }
        builder.append("\n");
        return builder.toString();
    }
}

