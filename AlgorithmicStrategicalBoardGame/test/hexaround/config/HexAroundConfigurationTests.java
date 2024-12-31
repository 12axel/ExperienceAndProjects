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

package hexaround.config;

import hexaround.game.*;
import hexaround.game.board.*;
import hexaround.game.hex.HexCoordinate;
import hexaround.game.player.*;
import hexaround.game.creature.*;
import static hexaround.game.creature.CreatureName.*;
import static hexaround.game.MoveResult.*;

import hexaround.game.rules.EnsureConnectedness;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.Collection;
import java.util.stream.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class HexAroundConfigurationTests {
    private static IHexAroundGameManager firstManager;

    @BeforeEach
    public void setupEach() throws IOException {
        firstManager = HexAroundGameBuilder.buildGameManager("testConfigurations/FirstConfiguration.hgc");
    }

    /*@Test
    void testConfigurationCanBeMadeFromHGCFile() throws IOException {
        String hgcFile = "testConfigurations/SecondConfiguration.hgc";
        HexAroundConfigurationMaker maker = new HexAroundConfigurationMaker(hgcFile);
        GameConfiguration gc = maker.makeConfiguration();
        assertTrue(true);
        System.out.println(gc.toString());
    }*/

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureToPlaceACreatureOnOccupiedHexProvider")
    void testCallPlaceCreatureToPlaceACreatureOnOccupiedHex(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(creature, 0, 0);
        state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallPlaceCreatureToPlaceACreatureOnOccupiedHexProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, MOVE_ERROR, 0, 0, "Hex is already occupied.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureToPlaceACreatureThatWasNeverInPlayerInventoryProvider")
    void testCallPlaceCreatureToPlaceACreatureThatWasNeverInPlayerInventory(CreatureName creature, MoveResult expectedResult, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(creature, x, y);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallPlaceCreatureToPlaceACreatureThatWasNeverInPlayerInventoryProvider() {
        return Stream.of(
                arguments(DOVE, MOVE_ERROR, 0, 0, "Creature not in player's inventory")
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureToPlaceACreatureThatIsOutOfStockFromPlayerInventoryProvider")
    void testCallPlaceCreatureToPlaceACreatureThatIsOutOfStockFromPlayerInventory(CreatureName creature, MoveResult expectedResult, int x, int y, String expectedMessage) {
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        GameState state = firstManager.placeCreature(creature, 0, 0);
        state = firstManager.placeCreature(creature, 0, 1);
        state = firstManager.placeCreature(creature, x, y);
        assertEquals(expectedResult, state.moveResult());
        assertNotNull(state.message());
        assertEquals(expectedMessage, state.message());
    }

    static Stream<Arguments> CallPlaceCreatureToPlaceACreatureThatIsOutOfStockFromPlayerInventoryProvider() {
        return Stream.of(
                arguments(BUTTERFLY, MOVE_ERROR, 0, -1, "Creature not in player's inventory")
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureToPerformAnInvalidFirstBluePlayerCreaturePlacementProvider")
    void testCallPlaceCreatureToPerformAnInvalidFirstBluePlayerCreaturePlacement(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallPlaceCreatureToPerformAnInvalidFirstBluePlayerCreaturePlacementProvider() {
        return Stream.of(
                arguments(BUTTERFLY, MOVE_ERROR, 0, 1, "Creature Placement Turn Rules have been broken.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureToPerformAValidFirstBluePlayerCreaturePlacementProvider")
    void testCallPlaceCreatureToPerformAValidFirstBluePlayerCreaturePlacement(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallPlaceCreatureToPerformAValidFirstBluePlayerCreaturePlacementProvider() {
        return Stream.of(
                arguments(BUTTERFLY, OK, 0, 0, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureToPerformAnInvalidFirstRedPlayerCreaturePlacementProvider")
    void testCallPlaceCreatureToPerformAnInvalidFirstRedPlayerCreaturePlacement(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(creature, 0, 0);
        state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallPlaceCreatureToPerformAnInvalidFirstRedPlayerCreaturePlacementProvider() {
        return Stream.of(
                arguments(HORSE, MOVE_ERROR, 1, 1, "Creature Placement Turn Rules have been broken.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureWithAValidFirstRedPlayerCreaturePlacementProvider")
    void testCallPlaceCreatureToPerformAValidFirstRedPlayerCreaturePlacement(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(creature, 0, 0);
        state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallPlaceCreatureWithAValidFirstRedPlayerCreaturePlacementProvider() {
        return Stream.of(
                arguments(HORSE, OK, 0, 1, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureToPerformAnInvalidPlayerCreaturePlacementBeyondFirstCreaturePlacementProvider")
    void testCallPlaceCreatureWithInvalidPlayerMoveBeyondFirstMove(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(creature, 0, 0);
        state = firstManager.placeCreature(creature,0,1);
        state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallPlaceCreatureToPerformAnInvalidPlayerCreaturePlacementBeyondFirstCreaturePlacementProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, MOVE_ERROR, 1, 0, "Creature Placement Turn Rules have been broken.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureToPerformValidPlayerCreaturePlacementBeyondFirstCreaturePlacementProvider")
    void testCallPlaceCreatureToPerformValidPlayerCreaturePlacementBeyondFirstCreaturePlacement(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(creature, 0, 0);
        state = firstManager.placeCreature(creature,0,1);
        state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallPlaceCreatureToPerformValidPlayerCreaturePlacementBeyondFirstCreaturePlacementProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, OK, 0, -1, null)
        );
    }

    @ParameterizedTest
    @MethodSource("testCallPlaceCreatureWithUntimelyBluePlayerButterflyPlacementProvider")
    void testCallPlaceCreatureWithUntimelyBluePlayerButterflyPlacement(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(GRASSHOPPER, 0, 0);
        state = firstManager.placeCreature(GRASSHOPPER,0,1);
        state = firstManager.placeCreature(GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(GRASSHOPPER,0,2);
        state = firstManager.placeCreature(GRASSHOPPER,0,-2);
        state = firstManager.placeCreature(GRASSHOPPER,0,3);
        state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> testCallPlaceCreatureWithUntimelyBluePlayerButterflyPlacementProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, MOVE_ERROR, 0, -3, "The butterfly has not been placed timely.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureWithTimelyBluePlayerButterflyPlacementProvider")
    void testCallPlaceCreatureWithTimelyBluePlayerButterflyPlacement(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(GRASSHOPPER, 0, 0);
        state = firstManager.placeCreature(GRASSHOPPER,0,1);
        state = firstManager.placeCreature(GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(GRASSHOPPER,0,2);
        state = firstManager.placeCreature(GRASSHOPPER,0,-2);
        state = firstManager.placeCreature(GRASSHOPPER,0,3);
        state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallPlaceCreatureWithTimelyBluePlayerButterflyPlacementProvider() {
        return Stream.of(
                arguments(BUTTERFLY, OK, 0, -3, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureWithUntimelyRedPlayerButterflyPlacementProvider")
    void testCallPlaceCreatureWithUntimelyRedPlayerButterflyPlacement(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(GRASSHOPPER, 0, 0);
        state = firstManager.placeCreature(GRASSHOPPER,0,1);
        state = firstManager.placeCreature(GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(GRASSHOPPER,0,2);
        state = firstManager.placeCreature(GRASSHOPPER,0,-2);
        state = firstManager.placeCreature(GRASSHOPPER,0,3);
        state = firstManager.placeCreature(BUTTERFLY,0,-3);
        state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallPlaceCreatureWithUntimelyRedPlayerButterflyPlacementProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, MOVE_ERROR, 0, 4, "The butterfly has not been placed timely.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureWithTimelyRedPlayerButterflyPlacementProvider")
    void testCallPlaceCreatureWithTimelyRedPlayerButterflyPlacement(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        GameState state = firstManager.placeCreature(GRASSHOPPER, 0, 0);
        state = firstManager.placeCreature(GRASSHOPPER,0,1);
        state = firstManager.placeCreature(GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(GRASSHOPPER,0,2);
        state = firstManager.placeCreature(GRASSHOPPER,0,-2);
        state = firstManager.placeCreature(GRASSHOPPER,0,3);
        state = firstManager.placeCreature(BUTTERFLY,0,-3);
        state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallPlaceCreatureWithTimelyRedPlayerButterflyPlacementProvider() {
        return Stream.of(
                arguments(BUTTERFLY, OK, 0, 4, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureWithCreaturePlacementCausingBlueButterflyToBeSurroundedProvider")
    void testCallPlaceCreatureWithCreaturePlacementCausingBlueButterflyToBeSurrounded(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        board.placeCreature(CreatureName.BUTTERFLY, new HexCoordinate(0,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, 1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, 0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, -1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, -1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(-1, 0), PlayerName.BLUE);
        for(int i = 0; i < 3; i++) {
            moveManager.incrementTurnNumber();
        }
        GameState state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result == MOVE_ERROR) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallPlaceCreatureWithCreaturePlacementCausingBlueButterflyToBeSurroundedProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, RED_WON, -1, 1, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureWithCreaturePlacementCausingRedButterflyToBeSurroundedProvider")
    void testCallPlaceCreatureWithCreaturePlacementCausingRedButterflyToBeSurrounded(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.BUTTERFLY, new HexCoordinate(0,0), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, 1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, 0), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, -1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, -1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(-1, 0), PlayerName.RED);
        playerManager.setCurrentPlayer(PlayerName.RED);
        for(int i = 0; i < 3; i++) {
            moveManager.incrementTurnNumber();
        }
        GameState state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result == MOVE_ERROR) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallPlaceCreatureWithCreaturePlacementCausingRedButterflyToBeSurroundedProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, BLUE_WON, -1, 1, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallPlaceCreatureToPlaceCreatureWithButterflyAlreadySurroundedProvider")
    void testCallPlaceCreatureToPlaceCreatureWithButterflyAlreadySurrounded(CreatureName creature, MoveResult result, int x, int y, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.BUTTERFLY, new HexCoordinate(0,0), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, 1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, 0), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, -1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, -1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(-1, 0), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(-1, 1), PlayerName.RED);
        playerManager.setCurrentPlayer(PlayerName.RED);
        for(int i = 0; i < 3; i++) {
            moveManager.incrementTurnNumber();
        }
        GameState state = firstManager.placeCreature(creature, x, y);
        assertEquals(result, state.moveResult());
        if (result == MOVE_ERROR) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallPlaceCreatureToPlaceCreatureWithButterflyAlreadySurroundedProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, MOVE_ERROR, 0, 2, "A butterfly is surrounded, the game is over.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveCreatureFromHexToSameHexProvider")
    void testCallMoveCreatureToMoveCreatureFromHexToSameHex(CreatureName creature, int x, int y, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.moveCreature(creature, x, y, x, y);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveCreatureFromHexToSameHexProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, 0, 0, MOVE_ERROR, "The 'from' hex is the same as the 'to' hex.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveCreatureFromEmptyHexProvider")
    void testCallMoveCreatureToMoveCreatureFromEmptyHex(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveCreatureFromEmptyHexProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, 1, 1, 2, 2, MOVE_ERROR, "There is no piece on the 'from' hex.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveCreatureThatIsNotTheSameAsTheOneOnTheFromHexProvider")
    void testCallMoveCreatureToMoveCreatureThatIsNotTheSameAsTheOneOnTheFromHex(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveCreatureThatIsNotTheSameAsTheOneOnTheFromHexProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, 0, 0, 1, 1, MOVE_ERROR, "The specified creature is not on the 'from' hex.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveCreatureToOccupiedHexProvider")
    void testCallMoveCreatureToMoveCreatureToOccupiedHex(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 2);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveCreatureToOccupiedHexProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, 0, -1, 0, 1, MOVE_ERROR, "The 'to' hex already has a creature on it.")
        );
    }


    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveCreatureThatDoesNotBelongToCurrentPlayerProvider")
    void testCallMoveCreatureToMoveCreatureThatDoesNotBelongToCurrentPlayer(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 2);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveCreatureThatDoesNotBelongToCurrentPlayerProvider() {
        return Stream.of(
                arguments(GRASSHOPPER, 0, 1, 0, -2, MOVE_ERROR, "Cannot move an opponent's creature.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveButterflyCreatureBeyondItsMaxDistanceProvider")
    void testCallMoveCreatureToMoveButterflyCreatureBeyondItsMaxDistance(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.placeCreature(CreatureName.TURTLE, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.BUTTERFLY, 1, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -2);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveButterflyCreatureBeyondItsMaxDistanceProvider() {
        return Stream.of(
                arguments(CreatureName.BUTTERFLY, 1, 1, 1, -1, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveButterflyCreatureToHexWhenGoingToItWouldBreakConnectednessAlongTheWayProvider")
    void testCallMoveCreatureToMoveButterflyCreatureToHexWhenGoingToItWouldBreakConnectednessAlongTheWay(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.placeCreature(CreatureName.TURTLE, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 1, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 1, -2);
        state = firstManager.placeCreature(CreatureName.BUTTERFLY, 2, 1);
        playerManager.setCurrentPlayer(PlayerName.RED);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveButterflyCreatureToHexWhenGoingToItWouldBreakConnectednessAlongTheWayProvider() {
        return Stream.of(
                arguments(CreatureName.BUTTERFLY, 2, 1, 3, 1, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveButterflyCreatureToHexWhenItCannotBeDraggedToProvider")
    void testCallMoveCreatureToMoveButterflyCreatureToHexWhenItCannotBeDraggedTo(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(-1,0), PlayerName.BLUE);
        GameState state = firstManager.placeCreature(BUTTERFLY, 0, 0);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveButterflyCreatureToHexWhenItCannotBeDraggedToProvider() {
        return Stream.of(
                arguments(CreatureName.BUTTERFLY, 0, 0, -1, 1, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToPerformAValidButterflyCreatureMoveProvider")
    void testCallMoveCreatureToPerformAValidButterflyCreatureMove(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,-1), PlayerName.BLUE);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToPerformAValidButterflyCreatureMoveProvider() {
        return Stream.of(
                arguments(CreatureName.BUTTERFLY, 0, 0, -1, 1, OK, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveWalkingCreatureBeyondItsMaxDistanceProvider")
    void testCallMoveCreatureToMoveWalkingCreatureBeyondItsMaxDistanceProvider(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.TURTLE, 1, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -2);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveWalkingCreatureBeyondItsMaxDistanceProvider() {
        return Stream.of(
                arguments(CreatureName.TURTLE, 1, 1, 1, -3, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveWalkingCreatureToHexWhenGoingToItWouldBreakConnectednessAlongTheWayProvider")
    void testCallMoveCreatureToMoveWalkingCreatureToHexWhenGoingToItWouldBreakConnectednessAlongTheWay(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 1, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 1, -2);
        state = firstManager.placeCreature(CreatureName.TURTLE, 2, 1);
        playerManager.setCurrentPlayer(PlayerName.RED);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveWalkingCreatureToHexWhenGoingToItWouldBreakConnectednessAlongTheWayProvider() {
        return Stream.of(
                arguments(CreatureName.TURTLE, 2, 1, 2, -2, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveWalkingCreatureToHexWhenItCannotBeDraggedThereProvider")
    void testCallMoveCreatureToMoveWalkingCreatureToHexWhenItCannotBeDraggedThere(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(-1,0), PlayerName.BLUE);

        GameState state = firstManager.placeCreature(TURTLE, 0, 0);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveWalkingCreatureToHexWhenItCannotBeDraggedThereProvider() {
        return Stream.of(
                arguments(CreatureName.TURTLE, 0, 0, -1, 1, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToPerformAValidWalkingCreatureMoveProvider")
    void testCallMoveCreatureToPerformAValidWalkingCreatureMove(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        GameState state = firstManager.placeCreature(CreatureName.TURTLE, 0, 0);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,-1), PlayerName.BLUE);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToPerformAValidWalkingCreatureMoveProvider() {
        return Stream.of(
                arguments(CreatureName.TURTLE, 0, 0, -1, 2, OK, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveFlyingCreatureBeyondItsMaxDistanceProvider")
    void testCallMoveCreatureToMoveFlyingCreatureBeyondItsMaxDistanceProvided(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,2), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,3), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,4), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,5), PlayerName.BLUE);
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.placeCreature(CreatureName.DUCK, 0, -1);
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveFlyingCreatureBeyondItsMaxDistanceProvider() {
        return Stream.of(
                arguments(CreatureName.DUCK, 0, -1, 0, 6, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveFlyingCreatureWhenAllItsNeighborsAreOccupiedProvider")
    void testCallMoveCreatureToMoveFlyingCreatureWhenAllItsNeighborsAreOccupied(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(-1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(-1,1), PlayerName.BLUE);
        GameState state = firstManager.placeCreature(CreatureName.DUCK, 0, 0);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveFlyingCreatureWhenAllItsNeighborsAreOccupiedProvider() {
        return Stream.of(
                arguments(CreatureName.DUCK, 0, 0, 0, 2, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveFlyingCreatureToAHexWhereConnectivityGetsBrokenProvider")
    void testCallMoveCreatureToMoveFlyingCreatureToAHexWhereConnectivityGetsBroken(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,-1), PlayerName.BLUE);
        GameState state = firstManager.placeCreature(CreatureName.DUCK, 0, 0);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveFlyingCreatureToAHexWhereConnectivityGetsBrokenProvider() {
        return Stream.of(
                arguments(CreatureName.DUCK, 0, 0, -2, 0, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToPerformAValidFlyingCreatureMoveProvider")
    void testCallMoveCreatureToPerformAValidFlyingCreatureMove (CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,2), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,-1), PlayerName.BLUE);
        GameState state = firstManager.placeCreature(CreatureName.DUCK, 0, 0);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToPerformAValidFlyingCreatureMoveProvider() {
        return Stream.of(
                arguments(CreatureName.DUCK, 0, 0, 0, 3, OK, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveJumpingCreatureBeyondItsMaxDistanceProvider")
    void testCallMoveCreatureToMoveJumpingCreatureBeyondItsMaxDistanceProvided(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,2), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,3), PlayerName.BLUE);
        GameState state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 0);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveJumpingCreatureBeyondItsMaxDistanceProvider() {
        return Stream.of(
                arguments(CreatureName.GRASSHOPPER, 0, 0, 0, 4, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveJumpingCreatureToAHexThatIsNotLinearToItsCurrentPositionProvider")
    void testCallMoveCreatureToMoveJumpingCreatureToAHexThatIsNotLinearToItsCurrentPosition(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,2), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,2), PlayerName.BLUE);
        GameState state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 0);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveJumpingCreatureToAHexThatIsNotLinearToItsCurrentPositionProvider() {
        return Stream.of(
                arguments(CreatureName.GRASSHOPPER, 0, 0, 1, 1, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveJumpingCreatureToAHexWhereConnectivityGetsBrokenProvider")
    void testCallMoveCreatureToMoveJumpingCreatureToAHexWhereConnectivityGetsBroken (CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        GameState state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 0);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveJumpingCreatureToAHexWhereConnectivityGetsBrokenProvider() {
        return Stream.of(
                arguments(CreatureName.GRASSHOPPER, 0, 0, 0, 3, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToPerformAValidJumpingCreatureMoveProvider")
    void testCallMoveCreatureToPerformAValidJumpingCreatureMove (CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,2), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(-1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(-1,1), PlayerName.BLUE);
        GameState state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 0);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToPerformAValidJumpingCreatureMoveProvider() {
        return Stream.of(
                arguments(CreatureName.GRASSHOPPER, 0, 0, 0, 3, OK, null),
                arguments(CreatureName.GRASSHOPPER, 0, 0, 2, -2, OK, null),
                arguments(CreatureName.GRASSHOPPER, 0, 0, -2, 0, OK, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveRunningCreatureAlongAPathWithANumberOfStepsThatDoNotEqualItsMaxDistanceProvider")
    void testCallMoveCreatureToMoveRunningCreatureBeyondItsMaxDistance(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.HORSE, 1, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -2);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveRunningCreatureAlongAPathWithANumberOfStepsThatDoNotEqualItsMaxDistanceProvider() {
        return Stream.of(
                arguments(CreatureName.HORSE, 1, 1, 1, -1, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveRunningCreatureToHexWhenGoingToItWouldBreakConnectednessAlongTheWayProvider")
    void testCallMoveCreatureToMoveRunningCreatureToHexWhenGoingToItWouldBreakConnectednessAlongTheWay(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 1, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -2);
        state = firstManager.placeCreature(CreatureName.HORSE, 2, 1);
        playerManager.setCurrentPlayer(PlayerName.RED);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveRunningCreatureToHexWhenGoingToItWouldBreakConnectednessAlongTheWayProvider() {
        return Stream.of(
                arguments(CreatureName.HORSE, 2, 1, 2, -3, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveRunningCreatureToHexWhenItCannotBeDraggedThereProvider")
    void testCallMoveCreatureToMoveRunningCreatureToHexWhenItCannotBeDraggedThere(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(-1,0), PlayerName.BLUE);

        GameState state = firstManager.placeCreature(CreatureName.HORSE, 0, 0);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToMoveRunningCreatureToHexWhenItCannotBeDraggedThereProvider() {
        return Stream.of(
                arguments(CreatureName.HORSE, 0, 0, -1, -1, MOVE_ERROR, "This move is not valid for the creature's movement rules.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToPerformAValidRunningCreatureMoveProvider")
    void testCallMoveCreatureToPerformAValidRunningCreatureMove(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        GameState state = firstManager.placeCreature(CreatureName.HORSE, 0, 0);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(1,-1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER,new HexCoordinate(0,-1), PlayerName.BLUE);
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        for(int i = 0; i < 3; i++)
        {
            moveManager.incrementTurnNumber();
        }
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToPerformAValidRunningCreatureMoveProvider() {
        return Stream.of(
                arguments(CreatureName.HORSE, 0, 0, 1, -2, OK, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToPerformAValidCreatureMoveWhenBluePlayerHasNotPlacedTheirButterflyTimelyProvider")
    void testCallMoveCreatureToPerformAValidCreatureMoveWhenBluePlayerHasNotPlacedTheirButterflyTimelyProvider (CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 0);
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 2);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -2);
        state = firstManager.placeCreature(CreatureName.TURTLE, 1, 1);
        playerManager.setCurrentPlayer(PlayerName.RED);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToPerformAValidCreatureMoveWhenBluePlayerHasNotPlacedTheirButterflyTimelyProvider() {
        return Stream.of(
                arguments(CreatureName.TURTLE, 1, 1, 1, 0, MOVE_ERROR, "The butterfly has not been placed timely.")
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToPerformACreatureMoveWhenBluePlayerHasPlacedTheirButterflyTimelyProvider")
    void testCallMoveCreatureToPerformACreatureMoveWhenBluePlayerHasPlacedTheirButterflyTimelyProvider (CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 2);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -2);
        state = firstManager.placeCreature(CreatureName.TURTLE, 1, 1);
        playerManager.setCurrentPlayer(PlayerName.RED);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToPerformACreatureMoveWhenBluePlayerHasPlacedTheirButterflyTimelyProvider() {
        return Stream.of(
                arguments(CreatureName.TURTLE, 1, 1, 1, 0, OK, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToPerformAValidCreatureMoveWhenRedPlayerHasNotPlacedTheirButterflyTimelyProvider")
    void testCallMoveCreatureToPerformAValidCreatureMoveWhenRedPlayerHasNotPlacedTheirButterflyTimelyProvider (CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 2);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -2);
        state = firstManager.placeCreature(CreatureName.TURTLE, 1, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -3);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToPerformAValidCreatureMoveWhenRedPlayerHasNotPlacedTheirButterflyTimelyProvider() {
        return Stream.of(
                arguments(CreatureName.TURTLE, 1, 1, 1, 0, MOVE_ERROR, "The butterfly has not been placed timely.")
        );
    }


    @ParameterizedTest
    @MethodSource("CallMoveCreatureToPerformAValidCreatureMoveWhenRedPlayerHasPlacedTheirButterflyTimelyProvider")
    void testCallMoveCreatureToPerformAValidCreatureMoveWhenRedPlayerHasPlacedTheirButterflyTimelyProvider (CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 2);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -2);
        state = firstManager.placeCreature(CreatureName.TURTLE, 1, 1);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -3);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, 3);
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, 0, -4);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult != MoveResult.OK) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }

    static Stream<Arguments> CallMoveCreatureToPerformAValidCreatureMoveWhenRedPlayerHasPlacedTheirButterflyTimelyProvider() {
        return Stream.of(
                arguments(CreatureName.TURTLE, 1, 1, 1, 0, OK, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureWithCreatureMovementCausingBlueButterflyToBeSurroundedProvider")
    void CallMoveCreatureWithCreatureMovementCausingBlueButterflyToBeSurrounded(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, 1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, 0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, -1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, -1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(-1, 0), PlayerName.BLUE);
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        for(int i = 0; i < 3; i++) {
            moveManager.incrementTurnNumber();
        }
        state = firstManager.placeCreature(CreatureName.DUCK, -1, -1);
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult == MOVE_ERROR) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallMoveCreatureWithCreatureMovementCausingBlueButterflyToBeSurroundedProvider() {
        return Stream.of(
                arguments(CreatureName.DUCK, -1, -1, -1, 1, RED_WON, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureWithCreatureMovementCausingRedButterflyToBeSurroundedProvider")
    void CallMoveCreatureWithCreatureMovementCausingRedButterflyToBeSurrounded(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        playerManager.setCurrentPlayer(PlayerName.RED);
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, 1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, 0), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, -1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, -1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(-1, 0), PlayerName.RED);
        playerManager.setCurrentPlayer(PlayerName.RED);
        for(int i = 0; i < 3; i++) {
            moveManager.incrementTurnNumber();
        }
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, -1, -1);
        playerManager.setCurrentPlayer(PlayerName.RED);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult == MOVE_ERROR) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallMoveCreatureWithCreatureMovementCausingRedButterflyToBeSurroundedProvider() {
        return Stream.of(
                arguments(CreatureName.GRASSHOPPER, -1, -1, -1, 1, BLUE_WON, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureWithCreatureMovementCausingBothPlayersButterfliesToBeSurroundedProvider")
    void testCallMoveCreatureWithCreatureMovementCausingBothPlayersButterfliesToBeSurrounded(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 1);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(-1, 2), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, 2), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, 1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, 0), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, -1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, -1), PlayerName.RED);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(-1, 0), PlayerName.RED);
       playerManager.setCurrentPlayer(PlayerName.RED);
        for(int i = 0; i < 3; i++) {
            moveManager.incrementTurnNumber();
        }
        state = firstManager.placeCreature(CreatureName.GRASSHOPPER, -1, -1);
        playerManager.setCurrentPlayer(PlayerName.RED);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult == MOVE_ERROR) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallMoveCreatureWithCreatureMovementCausingBothPlayersButterfliesToBeSurroundedProvider() {
        return Stream.of(
                arguments(CreatureName.GRASSHOPPER, -1, -1, -1, 1, DRAW, null)
        );
    }

    @ParameterizedTest
    @MethodSource("CallMoveCreatureToMoveCreatureAfterAButterflyIsSurrounded")
    void CallMoveCreatureToMoveCreatureAfterAButterflyIsSurrounded(CreatureName creature, int fromX, int fromY, int toX, int toY, MoveResult expectedResult, String expectedMessage) {
        HexBoard board = ((HexAroundGameManager) firstManager).getHexBoard();
        TurnManager moveManager = ((HexAroundGameManager) firstManager).getTurnManager();
        PlayerManager playerManager = ((HexAroundGameManager)firstManager).getPlayerManager();
        GameState state = firstManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, 1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, 0), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(1, -1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(0, -1), PlayerName.BLUE);
        board.placeCreature(CreatureName.GRASSHOPPER, new HexCoordinate(-1, 0), PlayerName.BLUE);
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        for(int i = 0; i < 3; i++) {
            moveManager.incrementTurnNumber();
        }
        state = firstManager.placeCreature(CreatureName.DUCK, -1, 1);
        playerManager.setCurrentPlayer(PlayerName.BLUE);
        state = firstManager.moveCreature(creature, fromX, fromY, toX, toY);
        assertEquals(expectedResult, state.moveResult());
        if (expectedResult == MOVE_ERROR) {
            assertNotNull(state.message());
            assertEquals(expectedMessage, state.message());
        } else {
            assertNull(state.message());
        }
    }
    static Stream<Arguments> CallMoveCreatureToMoveCreatureAfterAButterflyIsSurrounded() {
        return Stream.of(
                arguments(CreatureName.DUCK, -1, 1, -1, -1, MOVE_ERROR, "A butterfly is surrounded, the game is over.")
        );
    }
}
