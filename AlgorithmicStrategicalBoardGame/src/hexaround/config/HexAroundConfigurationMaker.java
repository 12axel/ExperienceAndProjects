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


import hexaround.game.creature.CreatureName;
import hexaround.game.creature.CreatureProperty;
import hexaround.game.player.PlayerName;
import hexaround.game.creature.*;
import org.antlr.v4.runtime.*;

import java.io.*;
import java.util.*;
import hexaround.config.HexAroundParser.*;
import org.antlr.v4.runtime.tree.*;

import static hexaround.game.creature.CreatureProperty.*;
import static hexaround.game.player.PlayerName.*;
import static hexaround.game.creature.CreatureName.*;

public class HexAroundConfigurationMaker extends HexAroundBaseVisitor<Void> {
    private GameConfiguration gameConfiguration;
    private StringBuilder configuration;
    private ParserRuleContext parseTree;
    private String configurationFile;
    private Collection<CreatureDefinition> creatureDefinitions;
    private List<PlayerConfiguration> playerConfigurations;
    private Map<CreatureName, Integer> creatureInventory;  // for each player
    private boolean redIsConfigured = false, blueIsConfigured = false;
    private List<String> errors;

    public HexAroundConfigurationMaker(String configurationFile) { // make configuration
        this.configurationFile = configurationFile;
    }
    public GameConfiguration makeConfiguration() throws IOException {
        parseTree = parseInput(configurationFile);
        initializeConfiguration();
        parseTree.accept(this); // This is where the tree walk begins
        if (!errors.isEmpty()) {
            System.err.println("Errors detected on input");
            for (String error : errors) {
                System.err.println("  " + error);
            }
            throw new ConfigurationException("Errors on input were found, check stderr");
        }
        if (playerConfigurations.size() == 1) {
            PlayerConfiguration pc0 = playerConfigurations.get(0);
            PlayerName pn = blueIsConfigured ? RED : BLUE;
            playerConfigurations.add(new PlayerConfiguration(pn, pc0.creatures()));
        }
        gameConfiguration = new GameConfiguration(creatureDefinitions, playerConfigurations);
        return gameConfiguration;
    }

    private ParserRuleContext parseInput(String configurationFile) throws IOException {
        CharStream input = CharStreams.fromFileName(configurationFile);
        HexAroundLexer lexer = new HexAroundLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HexAroundParser parser = new HexAroundParser(tokens);
        return parser.configuration();
    }

    private void initializeConfiguration() {
        creatureDefinitions = new LinkedList<CreatureDefinition>();
        playerConfigurations = new LinkedList<PlayerConfiguration>();
        errors = new LinkedList<String>();
    }

    /*
     * Now the actual visitors
     */

    /****************************************************
     * Add to the CreatureDefinitions collection
     * @param creatureDef the CreatureDefinition node.
     ****************************************************/
    @Override
    public Void visitCreatureDefinition(CreatureDefinitionContext creatureDef) {
        CreatureName creatureName = CreatureName.valueOf(
                creatureDef.creatureName().getText().toUpperCase());
        List<CreatureProperty> properties = new LinkedList<CreatureProperty>();
        boolean hasMovement = false;
        for (CreaturePropertyContext cpc : creatureDef.properties) {
            CreatureProperty prop = getPropertyByName(cpc);
            properties.add(prop);
        }
        checkForMovementProperty(creatureDef, properties);
        int distance = getCreatureDistance(creatureDef.maxDistance);
        creatureDefinitions.add(new CreatureDefinition(creatureName, distance, properties));
        return null;
    }

    private CreatureProperty getPropertyByName(CreaturePropertyContext cpc) {
        try {
            return CreatureProperty.valueOf(cpc.getText().toUpperCase());
        } catch (IllegalArgumentException e) {
            addError(cpc, String.format("For some reason, %s is  not a valid creature property.",
                    cpc.getText().toUpperCase()));
            return null;
        }
    }

    private int getCreatureDistance(Token distance) {
        return distance == null ? 1 : Integer.parseInt(distance.getText());
    }

    private void checkForMovementProperty(CreatureDefinitionContext creatureDef,
                                          Collection<CreatureProperty> properties) {
        boolean hasMovement = properties.contains(WALKING)
                || properties.contains(RUNNING)
                || properties.contains(FLYING)
                || properties.contains(JUMPING);
        if (!hasMovement) {
            addError(creatureDef,
                    String.format("Creature %s does not have a valid movement.",
                            creatureDef.creatureName().getText()));
        }
    }

    /****************************************************
     * Construct the playerConfigurations.
     * @param playerConfig the PlayerConfiguration node.
     ****************************************************/

    @Override
    public Void visitPlayerConfiguration(PlayerConfigurationContext playerConfig)
    {
        PlayerName playerName =
                PlayerName.valueOf(playerConfig.playerName().getText().toUpperCase());
        checkPlayerName(playerName, playerConfig);
        creatureInventory = new HashMap<CreatureName, Integer>();
        for (CreatureListContext clc : playerConfig.creatures) {
            clc.accept(this);
        }
        checkCreatureInventory(playerConfig);
        playerConfigurations.add(new PlayerConfiguration(playerName, creatureInventory));
        return null;
    }

    private void checkPlayerName(PlayerName playerName,
                                 PlayerConfigurationContext playerConfig) {
        if (playerName == BLUE) {
            if (blueIsConfigured) {
                addError(playerConfig, "BLUE is configured more than one time.");
            }
            blueIsConfigured = true;
        } else {
            if (redIsConfigured) {
                addError(playerConfig, "RED is configured more than one time.");
            }
            redIsConfigured = true;
        }
    }

    private void checkCreatureInventory(ParserRuleContext ctx) {
        boolean hasButterfly = creatureInventory.containsKey(BUTTERFLY);
        if (!hasButterfly) {
            addError(ctx, "This player does not have a BUTTERFLY");
        } else if (creatureInventory.get(BUTTERFLY) > 1) {
            addError(ctx, "This player has more than one BUTTERFLY");
        }
    }

    @Override
    public Void visitCreatureSpecification(CreatureSpecificationContext csc) {
        CreatureName name = CreatureName.valueOf(
                csc.creatureName().getText().toUpperCase());
        Integer count = Integer.parseInt(csc.count.getText());
        if (!validCreatureCheck(name)) {
            addError(csc, String.format(
                    "This player does not have %s in its inventory", name));
            return null;
        }
        if (creatureInventory.containsKey(name)) {
            addError(csc, "This player has more than one entry for " + name);
            return null;
        }
        creatureInventory.put(name, count);
        return null;
    }

    private boolean validCreatureCheck(CreatureName name) {
        for (CreatureDefinition cd : creatureDefinitions) {
            if (cd.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /*
     * General Helpers
     */

    private void addError(ParserRuleContext ctx, String msg)
    {
        errors.add("Line " + ctx.getStart().getLine() + ": " + msg);
    }

    private void addError(TerminalNode n, String msg)
    {
        errors.add("Line " + n.getSymbol().getLine() + ": " + msg);
    }
}
