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

package hexaround.game.hex;

import java.util.*;


//IMPORTANT: The code used for this class comes from my solution to the coordi-nation assignment.
public class HexCoordinate {
    private final int x;
    private final int y;

    /**
     * The code used for this class comes from my solution to the coordi-nation assignment.
     * I modified the code so that instead of the methods taking an ICoordinate as a parameter,
     * all of them now take a HexCoordinate as a parameter.
     * Because of this, I was free to also remove all of the casting operations
     * that I previously had in the methods.
     */

    /**
     * Constructor for HexCoordinate.
     * Creates a HexCoordinate with specified x and y coordinate values.
     *
     * @param x the X-coordinate value of this HexCoordinate.
     * @param y the Y-coordinate value of this HexCoordinate.
     */
    public HexCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Computes the distance to the another hex coordinate.
     *
     * @param otherCoordinate the hex coordinate to which the shortest distance is being calculated from.
     * @return the distance to the other coordinate.
     * The formula used for this method can be found at
     * {@link <a href="https://www.redblobgames.com/grids/hexagons/">...</a>}
     */
    public int distanceTo(HexCoordinate otherCoordinate) {
        return ((Math.abs(x - otherCoordinate.x)
                + Math.abs(x + y - otherCoordinate.x - otherCoordinate.y)
                + Math.abs(y - otherCoordinate.y)) / 2
        );
    }

    /**
     * @param otherCoordinate the hex coordinate to compare with the current hex coordinate for linear alignment.
     * @return true if the other hex coordinate is in a straight
     * line (defined by the coordinate type)
     */
    public boolean isLinear(HexCoordinate otherCoordinate) {
        return ((x == otherCoordinate.x)
                || (y == otherCoordinate.y)
                || ((otherCoordinate.x - x) + (otherCoordinate.y - y) == 0)
        );
    }

    /**
     * @return a collection of all of the hex coordinates that
     * are neighbors (usually adjacent coordinates) of this
     * hex coordinate
     */
    public Collection<HexCoordinate> neighbors() {
        List<HexCoordinate> neighbors = new LinkedList<>();
        neighbors.add(new HexCoordinate(x, y + 1));
        neighbors.add(new HexCoordinate(x + 1, y));
        neighbors.add(new HexCoordinate(x + 1, y - 1));
        neighbors.add(new HexCoordinate(x, y - 1));
        neighbors.add(new HexCoordinate(x - 1, y));
        neighbors.add(new HexCoordinate(x - 1, y + 1));
        return neighbors;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj instanceof HexCoordinate) {
            HexCoordinate otherHexCoordinate = (HexCoordinate) obj;
            isEqual = ((x == otherHexCoordinate.x) && (y == otherHexCoordinate.y));
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
