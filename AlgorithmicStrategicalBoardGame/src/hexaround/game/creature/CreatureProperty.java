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

public enum CreatureProperty {
    QUEEN("Butterfly"),
    WALKING("Walking"),
    RUNNING("Running"),
    FLYING("Flying"),
    JUMPING("Jumping"),
    INTRUDING("Intruding"),
    TRAPPING("Trapping"),
    SWAPPING("Swapping"),
    KAMIKAZE("Kamikaze"),
    HATCHING("Hatching");

    private final String name;

    private CreatureProperty(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
