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

import hexaround.config.*;
import hexaround.game.moves.*;
import java.util.*;
import java.util.function.*;
public class CreatureFactory {

    /**
     * The following sources were used for the implementation of this class:
     * {@link <a href="https://refactorizando.com/en/java-supplier-interface-and-factory-pattern/">...</a>}
     * {@link <a href="https://medium.com/@dev.thiago/factory-pattern-with-map-and-lambda-in-java-70e5383b8f20/">...</a>}
     * {@link <a href="https://www.geeksforgeeks.org/function-interface-in-java-with-examples/">...</a>}
     */

    private static final Map<CreatureProperty, Function<CreatureDefinition, Creature>> creatureMappings = new HashMap<>();

    static {
        creatureMappings.put(CreatureProperty.QUEEN, definition -> new WalkingCreature(definition, new WalkingMovementStrategy()));
        creatureMappings.put(CreatureProperty.WALKING, definition -> new WalkingCreature(definition, new WalkingMovementStrategy()));
        creatureMappings.put(CreatureProperty.RUNNING, definition -> new RunningCreature(definition, new RunningMovementStrategy()));
        creatureMappings.put(CreatureProperty.FLYING, definition -> new FlyingCreature(definition, new FlyingMovementStrategy()));
        creatureMappings.put(CreatureProperty.JUMPING, definition -> new JumpingCreature(definition, new JumpingMovementStrategy()));
    }


    /**
     * @param definition the creature definition containing the properties of the creature to create.
     * @return a new creature based on the provided definition.
     */
    public static Creature getCreature(CreatureDefinition definition){
        Creature creature = null;

        for (CreatureProperty property : definition.properties()){
            if(creatureMappings.containsKey(property)){
                creature = creatureMappings.get(property).apply(definition);
            }
        }

        return creature;
    }

    /**
     * @param creatureDefinitions a collection of CreatureDefinition objects to create creatures from.
     * @return a map of the creatures' names to their corresponding creature instances,
     * representing the created creatures.
     */
    public static Map<CreatureName, Creature> createCreatures(Collection<CreatureDefinition> creatureDefinitions) {
        Map<CreatureName, Creature> creatures = new HashMap<>();
        for (CreatureDefinition definition : creatureDefinitions) {
            creatures.put(definition.name(), getCreature(definition));
        }

        return creatures;
    }
}