// Generated from /Users/axelluca/Downloads/HexAround-B23 7/grammar/HexAround.g4 by ANTLR 4.13.1
package hexaround.config;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class HexAroundParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, BUTTERFLY=7, CRAB=8, DOVE=9, 
		DUCK=10, GRASSHOPPER=11, HORSE=12, HUMMINGBIRD=13, RABBIT=14, SPIDER=15, 
		TURTLE=16, FLYING=17, HATCHING=18, INTRUDING=19, JUMPING=20, KAMIKAZE=21, 
		QUEEN=22, RUNNING=23, SWAPPING=24, TRAPPING=25, WALKING=26, BLUE=27, RED=28, 
		WS=29, COMMENT=30, INTEGER=31, LETTER=32;
	public static final int
		RULE_configuration = 0, RULE_creatureDefinition = 1, RULE_creatureName = 2, 
		RULE_creatureProperty = 3, RULE_playerConfiguration = 4, RULE_playerName = 5, 
		RULE_creatureList = 6, RULE_creatureSpecList = 7, RULE_creatureSpecification = 8;
	private static String[] makeRuleNames() {
		return new String[] {
			"configuration", "creatureDefinition", "creatureName", "creatureProperty", 
			"playerConfiguration", "playerName", "creatureList", "creatureSpecList", 
			"creatureSpecification"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'CREATURES'", "':'", "'PLAYERS'", "'['", "']'", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, "BUTTERFLY", "CRAB", "DOVE", 
			"DUCK", "GRASSHOPPER", "HORSE", "HUMMINGBIRD", "RABBIT", "SPIDER", "TURTLE", 
			"FLYING", "HATCHING", "INTRUDING", "JUMPING", "KAMIKAZE", "QUEEN", "RUNNING", 
			"SWAPPING", "TRAPPING", "WALKING", "BLUE", "RED", "WS", "COMMENT", "INTEGER", 
			"LETTER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "HexAround.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public HexAroundParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConfigurationContext extends ParserRuleContext {
		public CreatureDefinitionContext creatureDefinition;
		public List<CreatureDefinitionContext> creatureDefs = new ArrayList<CreatureDefinitionContext>();
		public PlayerConfigurationContext playerConfiguration;
		public List<PlayerConfigurationContext> playerConfigurations = new ArrayList<PlayerConfigurationContext>();
		public TerminalNode EOF() { return getToken(HexAroundParser.EOF, 0); }
		public List<CreatureDefinitionContext> creatureDefinition() {
			return getRuleContexts(CreatureDefinitionContext.class);
		}
		public CreatureDefinitionContext creatureDefinition(int i) {
			return getRuleContext(CreatureDefinitionContext.class,i);
		}
		public List<PlayerConfigurationContext> playerConfiguration() {
			return getRuleContexts(PlayerConfigurationContext.class);
		}
		public PlayerConfigurationContext playerConfiguration(int i) {
			return getRuleContext(PlayerConfigurationContext.class,i);
		}
		public ConfigurationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_configuration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HexAroundVisitor ) return ((HexAroundVisitor<? extends T>)visitor).visitConfiguration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConfigurationContext configuration() throws RecognitionException {
		ConfigurationContext _localctx = new ConfigurationContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_configuration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			match(T__0);
			setState(19);
			match(T__1);
			setState(21); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(20);
				((ConfigurationContext)_localctx).creatureDefinition = creatureDefinition();
				((ConfigurationContext)_localctx).creatureDefs.add(((ConfigurationContext)_localctx).creatureDefinition);
				}
				}
				setState(23); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 130944L) != 0) );
			setState(25);
			match(T__2);
			setState(26);
			match(T__1);
			setState(28); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(27);
				((ConfigurationContext)_localctx).playerConfiguration = playerConfiguration();
				((ConfigurationContext)_localctx).playerConfigurations.add(((ConfigurationContext)_localctx).playerConfiguration);
				}
				}
				setState(30); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==BLUE || _la==RED );
			setState(32);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreatureDefinitionContext extends ParserRuleContext {
		public Token maxDistance;
		public CreaturePropertyContext creatureProperty;
		public List<CreaturePropertyContext> properties = new ArrayList<CreaturePropertyContext>();
		public CreatureNameContext creatureName() {
			return getRuleContext(CreatureNameContext.class,0);
		}
		public TerminalNode INTEGER() { return getToken(HexAroundParser.INTEGER, 0); }
		public List<CreaturePropertyContext> creatureProperty() {
			return getRuleContexts(CreaturePropertyContext.class);
		}
		public CreaturePropertyContext creatureProperty(int i) {
			return getRuleContext(CreaturePropertyContext.class,i);
		}
		public CreatureDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creatureDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HexAroundVisitor ) return ((HexAroundVisitor<? extends T>)visitor).visitCreatureDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatureDefinitionContext creatureDefinition() throws RecognitionException {
		CreatureDefinitionContext _localctx = new CreatureDefinitionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_creatureDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			creatureName();
			setState(36);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INTEGER) {
				{
				setState(35);
				((CreatureDefinitionContext)_localctx).maxDistance = match(INTEGER);
				}
			}

			setState(38);
			match(T__3);
			setState(40); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(39);
				((CreatureDefinitionContext)_localctx).creatureProperty = creatureProperty();
				((CreatureDefinitionContext)_localctx).properties.add(((CreatureDefinitionContext)_localctx).creatureProperty);
				}
				}
				setState(42); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 134086656L) != 0) );
			setState(44);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreatureNameContext extends ParserRuleContext {
		public TerminalNode BUTTERFLY() { return getToken(HexAroundParser.BUTTERFLY, 0); }
		public TerminalNode CRAB() { return getToken(HexAroundParser.CRAB, 0); }
		public TerminalNode DOVE() { return getToken(HexAroundParser.DOVE, 0); }
		public TerminalNode DUCK() { return getToken(HexAroundParser.DUCK, 0); }
		public TerminalNode GRASSHOPPER() { return getToken(HexAroundParser.GRASSHOPPER, 0); }
		public TerminalNode HORSE() { return getToken(HexAroundParser.HORSE, 0); }
		public TerminalNode HUMMINGBIRD() { return getToken(HexAroundParser.HUMMINGBIRD, 0); }
		public TerminalNode RABBIT() { return getToken(HexAroundParser.RABBIT, 0); }
		public TerminalNode SPIDER() { return getToken(HexAroundParser.SPIDER, 0); }
		public TerminalNode TURTLE() { return getToken(HexAroundParser.TURTLE, 0); }
		public CreatureNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creatureName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HexAroundVisitor ) return ((HexAroundVisitor<? extends T>)visitor).visitCreatureName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatureNameContext creatureName() throws RecognitionException {
		CreatureNameContext _localctx = new CreatureNameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_creatureName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 130944L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreaturePropertyContext extends ParserRuleContext {
		public TerminalNode FLYING() { return getToken(HexAroundParser.FLYING, 0); }
		public TerminalNode HATCHING() { return getToken(HexAroundParser.HATCHING, 0); }
		public TerminalNode INTRUDING() { return getToken(HexAroundParser.INTRUDING, 0); }
		public TerminalNode JUMPING() { return getToken(HexAroundParser.JUMPING, 0); }
		public TerminalNode KAMIKAZE() { return getToken(HexAroundParser.KAMIKAZE, 0); }
		public TerminalNode QUEEN() { return getToken(HexAroundParser.QUEEN, 0); }
		public TerminalNode RUNNING() { return getToken(HexAroundParser.RUNNING, 0); }
		public TerminalNode SWAPPING() { return getToken(HexAroundParser.SWAPPING, 0); }
		public TerminalNode TRAPPING() { return getToken(HexAroundParser.TRAPPING, 0); }
		public TerminalNode WALKING() { return getToken(HexAroundParser.WALKING, 0); }
		public CreaturePropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creatureProperty; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HexAroundVisitor ) return ((HexAroundVisitor<? extends T>)visitor).visitCreatureProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreaturePropertyContext creatureProperty() throws RecognitionException {
		CreaturePropertyContext _localctx = new CreaturePropertyContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_creatureProperty);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 134086656L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PlayerConfigurationContext extends ParserRuleContext {
		public CreatureListContext creatureList;
		public List<CreatureListContext> creatures = new ArrayList<CreatureListContext>();
		public PlayerNameContext playerName() {
			return getRuleContext(PlayerNameContext.class,0);
		}
		public CreatureListContext creatureList() {
			return getRuleContext(CreatureListContext.class,0);
		}
		public PlayerConfigurationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_playerConfiguration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HexAroundVisitor ) return ((HexAroundVisitor<? extends T>)visitor).visitPlayerConfiguration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PlayerConfigurationContext playerConfiguration() throws RecognitionException {
		PlayerConfigurationContext _localctx = new PlayerConfigurationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_playerConfiguration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			playerName();
			setState(51);
			((PlayerConfigurationContext)_localctx).creatureList = creatureList();
			((PlayerConfigurationContext)_localctx).creatures.add(((PlayerConfigurationContext)_localctx).creatureList);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PlayerNameContext extends ParserRuleContext {
		public TerminalNode RED() { return getToken(HexAroundParser.RED, 0); }
		public TerminalNode BLUE() { return getToken(HexAroundParser.BLUE, 0); }
		public PlayerNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_playerName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HexAroundVisitor ) return ((HexAroundVisitor<? extends T>)visitor).visitPlayerName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PlayerNameContext playerName() throws RecognitionException {
		PlayerNameContext _localctx = new PlayerNameContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_playerName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53);
			_la = _input.LA(1);
			if ( !(_la==BLUE || _la==RED) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreatureListContext extends ParserRuleContext {
		public CreatureSpecListContext creatureSpecList;
		public List<CreatureSpecListContext> creatureSpecs = new ArrayList<CreatureSpecListContext>();
		public CreatureSpecListContext creatureSpecList() {
			return getRuleContext(CreatureSpecListContext.class,0);
		}
		public CreatureListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creatureList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HexAroundVisitor ) return ((HexAroundVisitor<? extends T>)visitor).visitCreatureList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatureListContext creatureList() throws RecognitionException {
		CreatureListContext _localctx = new CreatureListContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_creatureList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			((CreatureListContext)_localctx).creatureSpecList = creatureSpecList();
			((CreatureListContext)_localctx).creatureSpecs.add(((CreatureListContext)_localctx).creatureSpecList);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreatureSpecListContext extends ParserRuleContext {
		public List<CreatureSpecificationContext> creatureSpecification() {
			return getRuleContexts(CreatureSpecificationContext.class);
		}
		public CreatureSpecificationContext creatureSpecification(int i) {
			return getRuleContext(CreatureSpecificationContext.class,i);
		}
		public CreatureSpecListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creatureSpecList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HexAroundVisitor ) return ((HexAroundVisitor<? extends T>)visitor).visitCreatureSpecList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatureSpecListContext creatureSpecList() throws RecognitionException {
		CreatureSpecListContext _localctx = new CreatureSpecListContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_creatureSpecList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			creatureSpecification();
			setState(62);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(58);
				match(T__5);
				setState(59);
				creatureSpecification();
				}
				}
				setState(64);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreatureSpecificationContext extends ParserRuleContext {
		public Token count;
		public CreatureNameContext creatureName() {
			return getRuleContext(CreatureNameContext.class,0);
		}
		public TerminalNode INTEGER() { return getToken(HexAroundParser.INTEGER, 0); }
		public CreatureSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creatureSpecification; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HexAroundVisitor ) return ((HexAroundVisitor<? extends T>)visitor).visitCreatureSpecification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatureSpecificationContext creatureSpecification() throws RecognitionException {
		CreatureSpecificationContext _localctx = new CreatureSpecificationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_creatureSpecification);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			creatureName();
			setState(66);
			((CreatureSpecificationContext)_localctx).count = match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001 E\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002"+
		"\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005"+
		"\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007"+
		"\b\u0001\u0000\u0001\u0000\u0001\u0000\u0004\u0000\u0016\b\u0000\u000b"+
		"\u0000\f\u0000\u0017\u0001\u0000\u0001\u0000\u0001\u0000\u0004\u0000\u001d"+
		"\b\u0000\u000b\u0000\f\u0000\u001e\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0003\u0001%\b\u0001\u0001\u0001\u0001\u0001\u0004\u0001"+
		")\b\u0001\u000b\u0001\f\u0001*\u0001\u0001\u0001\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0005\u0007=\b\u0007\n\u0007\f\u0007@\t\u0007\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0000\u0000\t\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0000"+
		"\u0003\u0001\u0000\u0007\u0010\u0001\u0000\u0011\u001a\u0001\u0000\u001b"+
		"\u001c@\u0000\u0012\u0001\u0000\u0000\u0000\u0002\"\u0001\u0000\u0000"+
		"\u0000\u0004.\u0001\u0000\u0000\u0000\u00060\u0001\u0000\u0000\u0000\b"+
		"2\u0001\u0000\u0000\u0000\n5\u0001\u0000\u0000\u0000\f7\u0001\u0000\u0000"+
		"\u0000\u000e9\u0001\u0000\u0000\u0000\u0010A\u0001\u0000\u0000\u0000\u0012"+
		"\u0013\u0005\u0001\u0000\u0000\u0013\u0015\u0005\u0002\u0000\u0000\u0014"+
		"\u0016\u0003\u0002\u0001\u0000\u0015\u0014\u0001\u0000\u0000\u0000\u0016"+
		"\u0017\u0001\u0000\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000\u0017"+
		"\u0018\u0001\u0000\u0000\u0000\u0018\u0019\u0001\u0000\u0000\u0000\u0019"+
		"\u001a\u0005\u0003\u0000\u0000\u001a\u001c\u0005\u0002\u0000\u0000\u001b"+
		"\u001d\u0003\b\u0004\u0000\u001c\u001b\u0001\u0000\u0000\u0000\u001d\u001e"+
		"\u0001\u0000\u0000\u0000\u001e\u001c\u0001\u0000\u0000\u0000\u001e\u001f"+
		"\u0001\u0000\u0000\u0000\u001f \u0001\u0000\u0000\u0000 !\u0005\u0000"+
		"\u0000\u0001!\u0001\u0001\u0000\u0000\u0000\"$\u0003\u0004\u0002\u0000"+
		"#%\u0005\u001f\u0000\u0000$#\u0001\u0000\u0000\u0000$%\u0001\u0000\u0000"+
		"\u0000%&\u0001\u0000\u0000\u0000&(\u0005\u0004\u0000\u0000\')\u0003\u0006"+
		"\u0003\u0000(\'\u0001\u0000\u0000\u0000)*\u0001\u0000\u0000\u0000*(\u0001"+
		"\u0000\u0000\u0000*+\u0001\u0000\u0000\u0000+,\u0001\u0000\u0000\u0000"+
		",-\u0005\u0005\u0000\u0000-\u0003\u0001\u0000\u0000\u0000./\u0007\u0000"+
		"\u0000\u0000/\u0005\u0001\u0000\u0000\u000001\u0007\u0001\u0000\u0000"+
		"1\u0007\u0001\u0000\u0000\u000023\u0003\n\u0005\u000034\u0003\f\u0006"+
		"\u00004\t\u0001\u0000\u0000\u000056\u0007\u0002\u0000\u00006\u000b\u0001"+
		"\u0000\u0000\u000078\u0003\u000e\u0007\u00008\r\u0001\u0000\u0000\u0000"+
		"9>\u0003\u0010\b\u0000:;\u0005\u0006\u0000\u0000;=\u0003\u0010\b\u0000"+
		"<:\u0001\u0000\u0000\u0000=@\u0001\u0000\u0000\u0000><\u0001\u0000\u0000"+
		"\u0000>?\u0001\u0000\u0000\u0000?\u000f\u0001\u0000\u0000\u0000@>\u0001"+
		"\u0000\u0000\u0000AB\u0003\u0004\u0002\u0000BC\u0005\u001f\u0000\u0000"+
		"C\u0011\u0001\u0000\u0000\u0000\u0005\u0017\u001e$*>";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}