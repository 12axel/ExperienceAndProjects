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
import hexaround.game.hex.HexCoordinate;
import hexaround.game.player.*;
import java.util.*;

public class HexBoard {

    private final Map<HexCoordinate, PlacedCreature> occupiedHexes;

    /**
     * Constructor for HexBoard.
     * Creates a HexBoard with an empty map of occupied hex coordinates.
     */
    public HexBoard() {
        this.occupiedHexes = new HashMap<>();
    }

    /**
     * Places a creature at a specified hex coordinate on the board.
     * @param creature the name of the creature to be placed.
     * @param coordinate the hex coordinate where the creature is to be placed.
     * @param player the player who owns the creature.
     */
    public void placeCreature(CreatureName creature, HexCoordinate coordinate, PlayerName player) {
        occupiedHexes.put(coordinate, new PlacedCreature(creature, player));
    }

    /**
     * @return true if there are no creatures on the board, false otherwise.
     */
    public boolean isEmpty() {
        return occupiedHexes.isEmpty();
    }

    /**
     * @return the set of all hex coordinates that are currently occupied by creatures on the board.
     */
    public Collection<HexCoordinate> getAllOccupiedHexes() {
        return occupiedHexes.keySet();
    }

    /**
     * @return the number of hexes that are currently occupied by creatures on the board.
     */
    public int getNumberOfOccupiedHexes() {
        return occupiedHexes.size();
    }

    /**
     * @param coordinate the specific hex coordinate on the board to check.
     * @return true if the specified coordinate on the board is occupied by a creature.
     */
    public boolean isHexOccupied(HexCoordinate coordinate) {
        return occupiedHexes.containsKey(coordinate);
    }

    /**
     * @param coordinate the specific hex coordinate on the board to retrieve the creature from.
     * @return the placed creature at the specified coordinate, or null if the coordinate is unoccupied.
     */
    public PlacedCreature getCreatureAt(HexCoordinate coordinate) {
        return occupiedHexes.get(coordinate);
    }

    /**
     * Removes a creature from a specified hex coordinate on the board.
     * @param coordinate The hex coordinate from which to remove the creature.
     */
    public void removeCreature(HexCoordinate coordinate) {
        occupiedHexes.remove(coordinate);
    }

    /**
     * @param coordinate the specific hex coordinate for which to check its neighbors.
     * @return true if all neighboring hexes are occupied.
     */
    public boolean areAllNeighborsOccupied(HexCoordinate coordinate) {
        boolean allOccupied = true;
        for (HexCoordinate neighbor : coordinate.neighbors()) {
            if (!isHexOccupied(neighbor)) {
                allOccupied = false;
            }
        }
        return allOccupied;
    }
}
