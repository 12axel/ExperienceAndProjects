// Generated from /Users/axelluca/Downloads/HexAround-B23 7/grammar/HexAround.g4 by ANTLR 4.13.1
package hexaround.config;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link HexAroundParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface HexAroundVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link HexAroundParser#configuration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConfiguration(HexAroundParser.ConfigurationContext ctx);
	/**
	 * Visit a parse tree produced by {@link HexAroundParser#creatureDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatureDefinition(HexAroundParser.CreatureDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link HexAroundParser#creatureName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatureName(HexAroundParser.CreatureNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link HexAroundParser#creatureProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatureProperty(HexAroundParser.CreaturePropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link HexAroundParser#playerConfiguration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlayerConfiguration(HexAroundParser.PlayerConfigurationContext ctx);
	/**
	 * Visit a parse tree produced by {@link HexAroundParser#playerName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlayerName(HexAroundParser.PlayerNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link HexAroundParser#creatureList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatureList(HexAroundParser.CreatureListContext ctx);
	/**
	 * Visit a parse tree produced by {@link HexAroundParser#creatureSpecList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatureSpecList(HexAroundParser.CreatureSpecListContext ctx);
	/**
	 * Visit a parse tree produced by {@link HexAroundParser#creatureSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatureSpecification(HexAroundParser.CreatureSpecificationContext ctx);
}