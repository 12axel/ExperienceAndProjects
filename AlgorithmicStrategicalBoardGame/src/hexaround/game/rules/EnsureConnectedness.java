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
import hexaround.game.creature.*;
import hexaround.game.hex.HexCoordinate;
import hexaround.game.player.*;
import java.util.*;

public class EnsureConnectedness {

    private final HexBoard hexBoard;
    private final EnforceDraggingRules enforceDraggingRules;

    /**
     * Constructor for the EnsureConnectedness class.
     * Creates an EnsureConnectedness object dedicated to
     * ensuring that all the game's creature connectedness
     * rules are being followed on the specified hex board.
     * @param hexBoard The hex board to be used for checking connectedness.
     */
    public EnsureConnectedness(HexBoard hexBoard) {
        this.hexBoard = hexBoard;
        this.enforceDraggingRules = new EnforceDraggingRules(hexBoard);
    }

    /**.
     * @return true if all occupied hexes on the board are all connected together.
     * The algorithm used for this method is a variation of Breadth First Search, which can be found at:
     * {@link <a href="https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/">...</a>}
     */
    public boolean areAllOccupiedHexesConnected() {
        boolean allConnected = true;

        if (!hexBoard.isEmpty()) {
            Set<HexCoordinate> exploredCoordinates = new HashSet<>();
            Queue<HexCoordinate> coordinatesToExplore = new LinkedList<>();
            HexCoordinate startingCoordinate = hexBoard.getAllOccupiedHexes().iterator().next();
            coordinatesToExplore.add(startingCoordinate);
            exploredCoordinates.add(startingCoordinate);

            while (!coordinatesToExplore.isEmpty()) {
                HexCoordinate currentCoordinate = coordinatesToExplore.poll();
                for (HexCoordinate neighboringCoordinate : currentCoordinate.neighbors()) {
                    if (hexBoard.isHexOccupied(neighboringCoordinate) && !exploredCoordinates.contains(neighboringCoordinate)) {
                        exploredCoordinates.add(neighboringCoordinate);
                        coordinatesToExplore.add(neighboringCoordinate);
                    }
                }
            }

            allConnected = (exploredCoordinates.size() == hexBoard.getNumberOfOccupiedHexes());
        }

        return allConnected;
    }

    /**
     * @param from the starting hex coordinate of the move.
     * @param to the destination hex coordinate of the move.
     * @return true if moving a creature from the "from" hex coordinate to the "to" hex
     * maintains the connectedness of all occupied hexes on the game board.
     */
    public boolean moveMaintainsConnectivity(HexCoordinate from, HexCoordinate to) {
        CreatureName creature = hexBoard.getCreatureAt(from).getCreatureName();
        PlayerName creatureOwner = hexBoard.getCreatureAt(from).getCreatureOwner();
        hexBoard.placeCreature(creature, to, creatureOwner);
        hexBoard.removeCreature(from);
        boolean areAllOccupiedHexesStillConnected = areAllOccupiedHexesConnected();
        hexBoard.placeCreature(creature, from, creatureOwner);
        hexBoard.removeCreature(to);
        return areAllOccupiedHexesStillConnected;
    }

    /**
     * @param startingCoordinate the starting hex coordinate of the exploration.
     * @param maxSteps the maximum number of steps from the starting hex coordinate to check for reachable coordinates.
     * @return A collection of reachable hex coordinates within the specified number of steps
     * where the dragging rules and connectivity is never broken
     * given the current state of the game board.
     * The algorithm used for this method is a variation of an algorithm that can be found at:
     * {@link <a href="https://www.redblobgames.com/grids/hexagons/">...</a>}
     */
    public Collection<HexCoordinate> findReachableHexesWithinNumberOfStepsWhereConnectivityIsMaintainedAtEachStep(HexCoordinate startingCoordinate, int maxSteps) {
        Queue<HexCoordinate> coordinatesToExplore = new LinkedList<>();
        Map<HexCoordinate, Integer> numberOfStepsFromStartingCoordinate = new HashMap<>();
        coordinatesToExplore.add(startingCoordinate);
        numberOfStepsFromStartingCoordinate.put(startingCoordinate, 0);

        while(!coordinatesToExplore.isEmpty()){
            HexCoordinate currentCoordinate = coordinatesToExplore.poll();
            int currentStep = numberOfStepsFromStartingCoordinate.get(currentCoordinate) + 1;
            if(currentStep <= maxSteps) {
                for (HexCoordinate neighboringCoordinate : enforceDraggingRules.findAvailableSpotsToDrag(currentCoordinate)) {
                    if (!numberOfStepsFromStartingCoordinate.containsKey(neighboringCoordinate) && moveMaintainsConnectivity(startingCoordinate, neighboringCoordinate)) {
                        coordinatesToExplore.add(neighboringCoordinate);
                        numberOfStepsFromStartingCoordinate.put(neighboringCoordinate, currentStep);
                    }
                }
            }
        }

        return numberOfStepsFromStartingCoordinate.keySet();
    }


    /**
     * @param startingCoordinate the starting hex coordinate of the exploration.
     * @param steps the number of steps from the starting hax coordinate to check for reachable coordinates.
     * @return A collection of hex coordinates that are exactly the specified number of steps away
     * where the dragging rules and connectivity are never broken
     * given the current state of the game board.
     * The algorithm used for this method, in combination with the explorePaths method,
     * is a variation of Depth First Search, which can be found at:
     * {@link <a href="https://www.baeldung.com/java-depth-first-search">...</a>}
     */
    public Collection<HexCoordinate> findReachableHexesInExactNumberOfStepsWhereConnectivityIsMaintainedAtEachStep(HexCoordinate startingCoordinate, int steps) {
        Set<HexCoordinate> reachableCoordinates = new HashSet<>();
        explorePathsWhereConnectivityIsMaintainedAtEachStep(startingCoordinate, startingCoordinate, steps, new HashSet<>(), reachableCoordinates);
        return reachableCoordinates;
    }

    /**
     * Helper method for recursively exploring paths from a starting hex coordinate
     * to hex coordinates in the specified number of steps exactly
     * where the dragging rules and connectivity are never broken
     * given the current state of the game board.
     * @param currentCoordinate the current hex coordinate being explored.
     * @param startingCoordinate the starting hex coordinate of the exploration.
     * @param remainingSteps the exact number of steps left to explore paths from the starting hex coordinate.
     * @param exploredCoordinates the set of already explored hex coordinates.
     * @param reachableCoordinates a set to store the result of reachable hex coordinates.
     */
    public void explorePathsWhereConnectivityIsMaintainedAtEachStep(HexCoordinate currentCoordinate, HexCoordinate startingCoordinate, int remainingSteps, Set<HexCoordinate> exploredCoordinates, Set<HexCoordinate> reachableCoordinates) {
        if (remainingSteps == 0) {
            reachableCoordinates.add(currentCoordinate);
            return;
        }

        for (HexCoordinate neighboringCoordinate : enforceDraggingRules.findAvailableSpotsToDrag(currentCoordinate)) {
            if (!exploredCoordinates.contains(neighboringCoordinate) && moveMaintainsConnectivity(startingCoordinate, neighboringCoordinate)) {
                exploredCoordinates.add(neighboringCoordinate);
                explorePathsWhereConnectivityIsMaintainedAtEachStep(neighboringCoordinate, startingCoordinate, remainingSteps - 1, exploredCoordinates, reachableCoordinates);
                exploredCoordinates.remove(neighboringCoordinate);
            }
        }
    }
}
