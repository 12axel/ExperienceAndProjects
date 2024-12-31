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

package hexaround.game;
import hexaround.game.player.PlayerName;
import hexaround.game.rules.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class GameOutcomeEvaluator {

    private final VictoryConditionsMonitor victoryConditionsMonitor;

    /**
     * Constructor for GameOutcomeEvaluator.
     * Creates a GameOutcomeEvaluator object, using the specified victoryConditionsMonitor object, that is
     * dedicated to evaluating which player won the game or if the game finished in a draw if the game's
     * victory conditions have been met.
     * @param victoryConditionsMonitor an object dedicated to monitoring and determining
     * whether the victory conditions in the game are met based on the state of the butterflies
     * on the game board.
     */
    public GameOutcomeEvaluator(VictoryConditionsMonitor victoryConditionsMonitor) {
        this.victoryConditionsMonitor = victoryConditionsMonitor;
    }

    /**
     * For this next method, the example code used as inspiration
     * for this method can be found at:
     * Office hour (13-Dec-2023)
     * {@link <a href="https://www.geeksforgeeks.org/supplier-interface-in-java-with-examples/">...</a>}
     */

    /**
     * @return a GameState based on which player won the game or if the game finished in a draw if the game's
     * victory conditions have been met.
     */
    public GameState evaluateGameOutcome() {
        GameState state = new GameState(MoveResult.OK);

        List<Supplier<Boolean>> victoryConditions = Arrays.asList(
                () -> victoryConditionsMonitor.bothButterfliesAreSurrounded(),
                () -> victoryConditionsMonitor.butterflyIsSurrounded(PlayerName.BLUE),
                () -> victoryConditionsMonitor.butterflyIsSurrounded(PlayerName.RED)
        );

        List<MoveResult> possibleGameOutcomes = Arrays.asList(
                MoveResult.DRAW,
                MoveResult.RED_WON,
                MoveResult.BLUE_WON
        );

        for(int i = 0; i < possibleGameOutcomes.size(); i++) {
            if(state.moveResult().equals(MoveResult.OK) && victoryConditions.get(i).get()) {
                state = new GameState(possibleGameOutcomes.get(i));
            }
        }

        return state;
    }
}
