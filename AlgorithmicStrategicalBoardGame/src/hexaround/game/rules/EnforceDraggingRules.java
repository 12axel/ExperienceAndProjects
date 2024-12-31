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

package hexaround.game.rules;

import hexaround.game.board.*;
import hexaround.game.hex.HexCoordinate;

import java.util.*;

public class EnforceDraggingRules {

    private final HexBoard hexBoard;

    /**
     * Constructor for the EnforceDraggingRules class.
     * Creates an EnforceDraggingRules object dedicated to
     * determining spots for which creatures can be "physically" dragged to on the specified hex board.
     * @param hexBoard The current hex board with the game's creatures and their locations.
     */
    public EnforceDraggingRules(HexBoard hexBoard) {
        this.hexBoard = hexBoard;
    }

    /**
     * @param coordinate the hex coordinate from which spots for which
     * creatures can be "physically" dragged to on the game board are to be determined.
     * @return a collection of hex coordinates representing the spots for which
     * creatures can be "physically" dragged to on the game board.
     */
    public Collection<HexCoordinate> findAvailableSpotsToDrag(HexCoordinate coordinate) {
        Set<HexCoordinate> draggableNeighbors = new HashSet<>();
        List<HexCoordinate> neighbors = new ArrayList<>(coordinate.neighbors());
        for (int i = 0; i < neighbors.size(); i++) {
            HexCoordinate currentNeighbor = neighbors.get(i);
            HexCoordinate nextNeighbor = neighbors.get((i + 1) % neighbors.size());
            if (!hexBoard.isHexOccupied(currentNeighbor) && !hexBoard.isHexOccupied(nextNeighbor)) {
                draggableNeighbors.add(currentNeighbor);
                draggableNeighbors.add(nextNeighbor);
            }
        }
        return draggableNeighbors;
    }
}

