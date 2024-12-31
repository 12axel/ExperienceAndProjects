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

public class CreatureCounter {
    private int count;

    /**
     * Constructor for CreatureCounter.
     * Creates a CreatureCounter with the specified initial count.
     * @param initialCount the initial count to set for the creature counter.
     */
    public CreatureCounter(int initialCount) {
        this.count = initialCount;
    }

    /**
     * Decrements the creature counter value by one.
     */
    public void decrementCount() {
        count--;
    }

    /**
     * @return the current creature counter value.
     */
    public int getCount() {
        return count;
    }
}

