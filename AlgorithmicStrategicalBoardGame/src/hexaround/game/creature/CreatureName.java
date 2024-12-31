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

package hexaround.game.creature;

/**
 * This enumeration just provides symbols for the different types of creatures
 * that are possible in a game of HexAround. Other than being a name, or symbol,
 * there is no other semantics associated with them. The specific properties of
 * creatures with that name depend upon the specific game configuration.
 */
public enum CreatureName {
    BUTTERFLY("Butterfly"),
    CRAB("Crab"),
    DOVE("Dove"),
    DUCK("Duck"),
    GRASSHOPPER("Grasshopper"),
    HORSE("Horse"),
    HUMMINGBIRD("Hummingbird"),
    RABBIT("Rabbit"),
    SPIDER("Spider"),
    TURTLE("Turtle")
    ;

    private final String name;

    private CreatureName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
