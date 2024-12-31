// Generated from /Users/axelluca/Downloads/HexAround-B23 7/grammar/HexAround.g4 by ANTLR 4.13.1
package hexaround.config;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class HexAroundLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "BUTTERFLY", "CRAB", 
			"DOVE", "DUCK", "GRASSHOPPER", "HORSE", "HUMMINGBIRD", "RABBIT", "SPIDER", 
			"TURTLE", "FLYING", "HATCHING", "INTRUDING", "JUMPING", "KAMIKAZE", "QUEEN", 
			"RUNNING", "SWAPPING", "TRAPPING", "WALKING", "BLUE", "RED", "WS", "COMMENT", 
			"INTEGER", "LETTER", "DIGIT"
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


	public HexAroundLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "HexAround.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000 \u01cc\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002"+
		"\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002"+
		"\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002"+
		"\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002"+
		"\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0003\u0006p\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007z\b"+
		"\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b"+
		"\u0003\b\u0084\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0003\t\u008e\b\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u00a6"+
		"\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u00b2"+
		"\b\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u00ca\b\f\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0003\r\u00d8\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00e6\b\u000e\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u00f4"+
		"\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0003\u0010\u0102\b\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0003\u0011\u0114\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0128\b\u0012\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u0138\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0003\u0014\u014a\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0003\u0015\u0156\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u0166"+
		"\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u0178"+
		"\b\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u018a"+
		"\b\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u019a\b\u0019\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0003\u001a\u01a4\b\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0003\u001b\u01ac\b\u001b\u0001\u001c\u0004"+
		"\u001c\u01af\b\u001c\u000b\u001c\f\u001c\u01b0\u0001\u001c\u0001\u001c"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0005\u001d"+
		"\u01ba\b\u001d\n\u001d\f\u001d\u01bd\t\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0004\u001e\u01c5\b\u001e\u000b"+
		"\u001e\f\u001e\u01c6\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001\u01bb"+
		"\u0000!\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b"+
		"\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b"+
		"\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016"+
		"-\u0017/\u00181\u00193\u001a5\u001b7\u001c9\u001d;\u001e=\u001f? A\u0000"+
		"\u0001\u0000\u0003\u0003\u0000\t\n\f\r  \u0002\u0000AZaz\u0001\u00000"+
		"9\u01e4\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000"+
		"\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000"+
		"\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000"+
		"\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000"+
		"\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000"+
		"\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000"+
		"\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000"+
		"\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000"+
		"!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001"+
		"\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001\u0000"+
		"\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-\u0001\u0000\u0000\u0000"+
		"\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000\u0000\u0000\u00003"+
		"\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000\u00007\u0001\u0000"+
		"\u0000\u0000\u00009\u0001\u0000\u0000\u0000\u0000;\u0001\u0000\u0000\u0000"+
		"\u0000=\u0001\u0000\u0000\u0000\u0000?\u0001\u0000\u0000\u0000\u0001C"+
		"\u0001\u0000\u0000\u0000\u0003M\u0001\u0000\u0000\u0000\u0005O\u0001\u0000"+
		"\u0000\u0000\u0007W\u0001\u0000\u0000\u0000\tY\u0001\u0000\u0000\u0000"+
		"\u000b[\u0001\u0000\u0000\u0000\ro\u0001\u0000\u0000\u0000\u000fy\u0001"+
		"\u0000\u0000\u0000\u0011\u0083\u0001\u0000\u0000\u0000\u0013\u008d\u0001"+
		"\u0000\u0000\u0000\u0015\u00a5\u0001\u0000\u0000\u0000\u0017\u00b1\u0001"+
		"\u0000\u0000\u0000\u0019\u00c9\u0001\u0000\u0000\u0000\u001b\u00d7\u0001"+
		"\u0000\u0000\u0000\u001d\u00e5\u0001\u0000\u0000\u0000\u001f\u00f3\u0001"+
		"\u0000\u0000\u0000!\u0101\u0001\u0000\u0000\u0000#\u0113\u0001\u0000\u0000"+
		"\u0000%\u0127\u0001\u0000\u0000\u0000\'\u0137\u0001\u0000\u0000\u0000"+
		")\u0149\u0001\u0000\u0000\u0000+\u0155\u0001\u0000\u0000\u0000-\u0165"+
		"\u0001\u0000\u0000\u0000/\u0177\u0001\u0000\u0000\u00001\u0189\u0001\u0000"+
		"\u0000\u00003\u0199\u0001\u0000\u0000\u00005\u01a3\u0001\u0000\u0000\u0000"+
		"7\u01ab\u0001\u0000\u0000\u00009\u01ae\u0001\u0000\u0000\u0000;\u01b4"+
		"\u0001\u0000\u0000\u0000=\u01c4\u0001\u0000\u0000\u0000?\u01c8\u0001\u0000"+
		"\u0000\u0000A\u01ca\u0001\u0000\u0000\u0000CD\u0005C\u0000\u0000DE\u0005"+
		"R\u0000\u0000EF\u0005E\u0000\u0000FG\u0005A\u0000\u0000GH\u0005T\u0000"+
		"\u0000HI\u0005U\u0000\u0000IJ\u0005R\u0000\u0000JK\u0005E\u0000\u0000"+
		"KL\u0005S\u0000\u0000L\u0002\u0001\u0000\u0000\u0000MN\u0005:\u0000\u0000"+
		"N\u0004\u0001\u0000\u0000\u0000OP\u0005P\u0000\u0000PQ\u0005L\u0000\u0000"+
		"QR\u0005A\u0000\u0000RS\u0005Y\u0000\u0000ST\u0005E\u0000\u0000TU\u0005"+
		"R\u0000\u0000UV\u0005S\u0000\u0000V\u0006\u0001\u0000\u0000\u0000WX\u0005"+
		"[\u0000\u0000X\b\u0001\u0000\u0000\u0000YZ\u0005]\u0000\u0000Z\n\u0001"+
		"\u0000\u0000\u0000[\\\u0005,\u0000\u0000\\\f\u0001\u0000\u0000\u0000]"+
		"^\u0005B\u0000\u0000^_\u0005U\u0000\u0000_`\u0005T\u0000\u0000`a\u0005"+
		"T\u0000\u0000ab\u0005E\u0000\u0000bc\u0005R\u0000\u0000cd\u0005F\u0000"+
		"\u0000de\u0005L\u0000\u0000ep\u0005Y\u0000\u0000fg\u0005b\u0000\u0000"+
		"gh\u0005u\u0000\u0000hi\u0005t\u0000\u0000ij\u0005t\u0000\u0000jk\u0005"+
		"e\u0000\u0000kl\u0005r\u0000\u0000lm\u0005f\u0000\u0000mn\u0005l\u0000"+
		"\u0000np\u0005y\u0000\u0000o]\u0001\u0000\u0000\u0000of\u0001\u0000\u0000"+
		"\u0000p\u000e\u0001\u0000\u0000\u0000qr\u0005C\u0000\u0000rs\u0005R\u0000"+
		"\u0000st\u0005A\u0000\u0000tz\u0005B\u0000\u0000uv\u0005c\u0000\u0000"+
		"vw\u0005r\u0000\u0000wx\u0005a\u0000\u0000xz\u0005b\u0000\u0000yq\u0001"+
		"\u0000\u0000\u0000yu\u0001\u0000\u0000\u0000z\u0010\u0001\u0000\u0000"+
		"\u0000{|\u0005D\u0000\u0000|}\u0005O\u0000\u0000}~\u0005V\u0000\u0000"+
		"~\u0084\u0005E\u0000\u0000\u007f\u0080\u0005d\u0000\u0000\u0080\u0081"+
		"\u0005o\u0000\u0000\u0081\u0082\u0005v\u0000\u0000\u0082\u0084\u0005e"+
		"\u0000\u0000\u0083{\u0001\u0000\u0000\u0000\u0083\u007f\u0001\u0000\u0000"+
		"\u0000\u0084\u0012\u0001\u0000\u0000\u0000\u0085\u0086\u0005D\u0000\u0000"+
		"\u0086\u0087\u0005U\u0000\u0000\u0087\u0088\u0005C\u0000\u0000\u0088\u008e"+
		"\u0005K\u0000\u0000\u0089\u008a\u0005d\u0000\u0000\u008a\u008b\u0005u"+
		"\u0000\u0000\u008b\u008c\u0005c\u0000\u0000\u008c\u008e\u0005k\u0000\u0000"+
		"\u008d\u0085\u0001\u0000\u0000\u0000\u008d\u0089\u0001\u0000\u0000\u0000"+
		"\u008e\u0014\u0001\u0000\u0000\u0000\u008f\u0090\u0005G\u0000\u0000\u0090"+
		"\u0091\u0005R\u0000\u0000\u0091\u0092\u0005A\u0000\u0000\u0092\u0093\u0005"+
		"S\u0000\u0000\u0093\u0094\u0005S\u0000\u0000\u0094\u0095\u0005H\u0000"+
		"\u0000\u0095\u0096\u0005O\u0000\u0000\u0096\u0097\u0005P\u0000\u0000\u0097"+
		"\u0098\u0005P\u0000\u0000\u0098\u0099\u0005E\u0000\u0000\u0099\u00a6\u0005"+
		"R\u0000\u0000\u009a\u009b\u0005g\u0000\u0000\u009b\u009c\u0005r\u0000"+
		"\u0000\u009c\u009d\u0005a\u0000\u0000\u009d\u009e\u0005s\u0000\u0000\u009e"+
		"\u009f\u0005s\u0000\u0000\u009f\u00a0\u0005h\u0000\u0000\u00a0\u00a1\u0005"+
		"o\u0000\u0000\u00a1\u00a2\u0005p\u0000\u0000\u00a2\u00a3\u0005p\u0000"+
		"\u0000\u00a3\u00a4\u0005e\u0000\u0000\u00a4\u00a6\u0005r\u0000\u0000\u00a5"+
		"\u008f\u0001\u0000\u0000\u0000\u00a5\u009a\u0001\u0000\u0000\u0000\u00a6"+
		"\u0016\u0001\u0000\u0000\u0000\u00a7\u00a8\u0005H\u0000\u0000\u00a8\u00a9"+
		"\u0005O\u0000\u0000\u00a9\u00aa\u0005R\u0000\u0000\u00aa\u00ab\u0005S"+
		"\u0000\u0000\u00ab\u00b2\u0005E\u0000\u0000\u00ac\u00ad\u0005h\u0000\u0000"+
		"\u00ad\u00ae\u0005o\u0000\u0000\u00ae\u00af\u0005r\u0000\u0000\u00af\u00b0"+
		"\u0005s\u0000\u0000\u00b0\u00b2\u0005e\u0000\u0000\u00b1\u00a7\u0001\u0000"+
		"\u0000\u0000\u00b1\u00ac\u0001\u0000\u0000\u0000\u00b2\u0018\u0001\u0000"+
		"\u0000\u0000\u00b3\u00b4\u0005H\u0000\u0000\u00b4\u00b5\u0005U\u0000\u0000"+
		"\u00b5\u00b6\u0005M\u0000\u0000\u00b6\u00b7\u0005M\u0000\u0000\u00b7\u00b8"+
		"\u0005I\u0000\u0000\u00b8\u00b9\u0005N\u0000\u0000\u00b9\u00ba\u0005G"+
		"\u0000\u0000\u00ba\u00bb\u0005B\u0000\u0000\u00bb\u00bc\u0005I\u0000\u0000"+
		"\u00bc\u00bd\u0005R\u0000\u0000\u00bd\u00ca\u0005D\u0000\u0000\u00be\u00bf"+
		"\u0005h\u0000\u0000\u00bf\u00c0\u0005u\u0000\u0000\u00c0\u00c1\u0005m"+
		"\u0000\u0000\u00c1\u00c2\u0005m\u0000\u0000\u00c2\u00c3\u0005i\u0000\u0000"+
		"\u00c3\u00c4\u0005n\u0000\u0000\u00c4\u00c5\u0005g\u0000\u0000\u00c5\u00c6"+
		"\u0005b\u0000\u0000\u00c6\u00c7\u0005i\u0000\u0000\u00c7\u00c8\u0005r"+
		"\u0000\u0000\u00c8\u00ca\u0005d\u0000\u0000\u00c9\u00b3\u0001\u0000\u0000"+
		"\u0000\u00c9\u00be\u0001\u0000\u0000\u0000\u00ca\u001a\u0001\u0000\u0000"+
		"\u0000\u00cb\u00cc\u0005R\u0000\u0000\u00cc\u00cd\u0005A\u0000\u0000\u00cd"+
		"\u00ce\u0005B\u0000\u0000\u00ce\u00cf\u0005B\u0000\u0000\u00cf\u00d0\u0005"+
		"I\u0000\u0000\u00d0\u00d8\u0005T\u0000\u0000\u00d1\u00d2\u0005r\u0000"+
		"\u0000\u00d2\u00d3\u0005a\u0000\u0000\u00d3\u00d4\u0005b\u0000\u0000\u00d4"+
		"\u00d5\u0005b\u0000\u0000\u00d5\u00d6\u0005i\u0000\u0000\u00d6\u00d8\u0005"+
		"t\u0000\u0000\u00d7\u00cb\u0001\u0000\u0000\u0000\u00d7\u00d1\u0001\u0000"+
		"\u0000\u0000\u00d8\u001c\u0001\u0000\u0000\u0000\u00d9\u00da\u0005S\u0000"+
		"\u0000\u00da\u00db\u0005P\u0000\u0000\u00db\u00dc\u0005I\u0000\u0000\u00dc"+
		"\u00dd\u0005D\u0000\u0000\u00dd\u00de\u0005E\u0000\u0000\u00de\u00e6\u0005"+
		"R\u0000\u0000\u00df\u00e0\u0005s\u0000\u0000\u00e0\u00e1\u0005p\u0000"+
		"\u0000\u00e1\u00e2\u0005i\u0000\u0000\u00e2\u00e3\u0005d\u0000\u0000\u00e3"+
		"\u00e4\u0005e\u0000\u0000\u00e4\u00e6\u0005r\u0000\u0000\u00e5\u00d9\u0001"+
		"\u0000\u0000\u0000\u00e5\u00df\u0001\u0000\u0000\u0000\u00e6\u001e\u0001"+
		"\u0000\u0000\u0000\u00e7\u00e8\u0005T\u0000\u0000\u00e8\u00e9\u0005U\u0000"+
		"\u0000\u00e9\u00ea\u0005R\u0000\u0000\u00ea\u00eb\u0005T\u0000\u0000\u00eb"+
		"\u00ec\u0005L\u0000\u0000\u00ec\u00f4\u0005E\u0000\u0000\u00ed\u00ee\u0005"+
		"t\u0000\u0000\u00ee\u00ef\u0005u\u0000\u0000\u00ef\u00f0\u0005r\u0000"+
		"\u0000\u00f0\u00f1\u0005t\u0000\u0000\u00f1\u00f2\u0005l\u0000\u0000\u00f2"+
		"\u00f4\u0005e\u0000\u0000\u00f3\u00e7\u0001\u0000\u0000\u0000\u00f3\u00ed"+
		"\u0001\u0000\u0000\u0000\u00f4 \u0001\u0000\u0000\u0000\u00f5\u00f6\u0005"+
		"F\u0000\u0000\u00f6\u00f7\u0005L\u0000\u0000\u00f7\u00f8\u0005Y\u0000"+
		"\u0000\u00f8\u00f9\u0005I\u0000\u0000\u00f9\u00fa\u0005N\u0000\u0000\u00fa"+
		"\u0102\u0005G\u0000\u0000\u00fb\u00fc\u0005f\u0000\u0000\u00fc\u00fd\u0005"+
		"l\u0000\u0000\u00fd\u00fe\u0005y\u0000\u0000\u00fe\u00ff\u0005i\u0000"+
		"\u0000\u00ff\u0100\u0005n\u0000\u0000\u0100\u0102\u0005g\u0000\u0000\u0101"+
		"\u00f5\u0001\u0000\u0000\u0000\u0101\u00fb\u0001\u0000\u0000\u0000\u0102"+
		"\"\u0001\u0000\u0000\u0000\u0103\u0104\u0005H\u0000\u0000\u0104\u0105"+
		"\u0005A\u0000\u0000\u0105\u0106\u0005T\u0000\u0000\u0106\u0107\u0005C"+
		"\u0000\u0000\u0107\u0108\u0005H\u0000\u0000\u0108\u0109\u0005I\u0000\u0000"+
		"\u0109\u010a\u0005N\u0000\u0000\u010a\u0114\u0005G\u0000\u0000\u010b\u010c"+
		"\u0005h\u0000\u0000\u010c\u010d\u0005a\u0000\u0000\u010d\u010e\u0005t"+
		"\u0000\u0000\u010e\u010f\u0005c\u0000\u0000\u010f\u0110\u0005h\u0000\u0000"+
		"\u0110\u0111\u0005i\u0000\u0000\u0111\u0112\u0005n\u0000\u0000\u0112\u0114"+
		"\u0005g\u0000\u0000\u0113\u0103\u0001\u0000\u0000\u0000\u0113\u010b\u0001"+
		"\u0000\u0000\u0000\u0114$\u0001\u0000\u0000\u0000\u0115\u0116\u0005I\u0000"+
		"\u0000\u0116\u0117\u0005N\u0000\u0000\u0117\u0118\u0005T\u0000\u0000\u0118"+
		"\u0119\u0005R\u0000\u0000\u0119\u011a\u0005U\u0000\u0000\u011a\u011b\u0005"+
		"D\u0000\u0000\u011b\u011c\u0005I\u0000\u0000\u011c\u011d\u0005N\u0000"+
		"\u0000\u011d\u0128\u0005G\u0000\u0000\u011e\u011f\u0005i\u0000\u0000\u011f"+
		"\u0120\u0005n\u0000\u0000\u0120\u0121\u0005t\u0000\u0000\u0121\u0122\u0005"+
		"r\u0000\u0000\u0122\u0123\u0005u\u0000\u0000\u0123\u0124\u0005d\u0000"+
		"\u0000\u0124\u0125\u0005i\u0000\u0000\u0125\u0126\u0005n\u0000\u0000\u0126"+
		"\u0128\u0005g\u0000\u0000\u0127\u0115\u0001\u0000\u0000\u0000\u0127\u011e"+
		"\u0001\u0000\u0000\u0000\u0128&\u0001\u0000\u0000\u0000\u0129\u012a\u0005"+
		"J\u0000\u0000\u012a\u012b\u0005U\u0000\u0000\u012b\u012c\u0005M\u0000"+
		"\u0000\u012c\u012d\u0005P\u0000\u0000\u012d\u012e\u0005I\u0000\u0000\u012e"+
		"\u012f\u0005N\u0000\u0000\u012f\u0138\u0005G\u0000\u0000\u0130\u0131\u0005"+
		"j\u0000\u0000\u0131\u0132\u0005u\u0000\u0000\u0132\u0133\u0005m\u0000"+
		"\u0000\u0133\u0134\u0005p\u0000\u0000\u0134\u0135\u0005i\u0000\u0000\u0135"+
		"\u0136\u0005n\u0000\u0000\u0136\u0138\u0005g\u0000\u0000\u0137\u0129\u0001"+
		"\u0000\u0000\u0000\u0137\u0130\u0001\u0000\u0000\u0000\u0138(\u0001\u0000"+
		"\u0000\u0000\u0139\u013a\u0005K\u0000\u0000\u013a\u013b\u0005A\u0000\u0000"+
		"\u013b\u013c\u0005M\u0000\u0000\u013c\u013d\u0005I\u0000\u0000\u013d\u013e"+
		"\u0005K\u0000\u0000\u013e\u013f\u0005A\u0000\u0000\u013f\u0140\u0005Z"+
		"\u0000\u0000\u0140\u014a\u0005E\u0000\u0000\u0141\u0142\u0005k\u0000\u0000"+
		"\u0142\u0143\u0005a\u0000\u0000\u0143\u0144\u0005m\u0000\u0000\u0144\u0145"+
		"\u0005i\u0000\u0000\u0145\u0146\u0005k\u0000\u0000\u0146\u0147\u0005a"+
		"\u0000\u0000\u0147\u0148\u0005z\u0000\u0000\u0148\u014a\u0005e\u0000\u0000"+
		"\u0149\u0139\u0001\u0000\u0000\u0000\u0149\u0141\u0001\u0000\u0000\u0000"+
		"\u014a*\u0001\u0000\u0000\u0000\u014b\u014c\u0005Q\u0000\u0000\u014c\u014d"+
		"\u0005U\u0000\u0000\u014d\u014e\u0005E\u0000\u0000\u014e\u014f\u0005E"+
		"\u0000\u0000\u014f\u0156\u0005N\u0000\u0000\u0150\u0151\u0005q\u0000\u0000"+
		"\u0151\u0152\u0005u\u0000\u0000\u0152\u0153\u0005e\u0000\u0000\u0153\u0154"+
		"\u0005e\u0000\u0000\u0154\u0156\u0005n\u0000\u0000\u0155\u014b\u0001\u0000"+
		"\u0000\u0000\u0155\u0150\u0001\u0000\u0000\u0000\u0156,\u0001\u0000\u0000"+
		"\u0000\u0157\u0158\u0005R\u0000\u0000\u0158\u0159\u0005U\u0000\u0000\u0159"+
		"\u015a\u0005N\u0000\u0000\u015a\u015b\u0005N\u0000\u0000\u015b\u015c\u0005"+
		"I\u0000\u0000\u015c\u015d\u0005N\u0000\u0000\u015d\u0166\u0005G\u0000"+
		"\u0000\u015e\u015f\u0005r\u0000\u0000\u015f\u0160\u0005u\u0000\u0000\u0160"+
		"\u0161\u0005n\u0000\u0000\u0161\u0162\u0005n\u0000\u0000\u0162\u0163\u0005"+
		"i\u0000\u0000\u0163\u0164\u0005n\u0000\u0000\u0164\u0166\u0005g\u0000"+
		"\u0000\u0165\u0157\u0001\u0000\u0000\u0000\u0165\u015e\u0001\u0000\u0000"+
		"\u0000\u0166.\u0001\u0000\u0000\u0000\u0167\u0168\u0005S\u0000\u0000\u0168"+
		"\u0169\u0005W\u0000\u0000\u0169\u016a\u0005A\u0000\u0000\u016a\u016b\u0005"+
		"P\u0000\u0000\u016b\u016c\u0005P\u0000\u0000\u016c\u016d\u0005I\u0000"+
		"\u0000\u016d\u016e\u0005N\u0000\u0000\u016e\u0178\u0005G\u0000\u0000\u016f"+
		"\u0170\u0005s\u0000\u0000\u0170\u0171\u0005w\u0000\u0000\u0171\u0172\u0005"+
		"a\u0000\u0000\u0172\u0173\u0005p\u0000\u0000\u0173\u0174\u0005p\u0000"+
		"\u0000\u0174\u0175\u0005i\u0000\u0000\u0175\u0176\u0005n\u0000\u0000\u0176"+
		"\u0178\u0005g\u0000\u0000\u0177\u0167\u0001\u0000\u0000\u0000\u0177\u016f"+
		"\u0001\u0000\u0000\u0000\u01780\u0001\u0000\u0000\u0000\u0179\u017a\u0005"+
		"T\u0000\u0000\u017a\u017b\u0005R\u0000\u0000\u017b\u017c\u0005A\u0000"+
		"\u0000\u017c\u017d\u0005P\u0000\u0000\u017d\u017e\u0005P\u0000\u0000\u017e"+
		"\u017f\u0005I\u0000\u0000\u017f\u0180\u0005N\u0000\u0000\u0180\u018a\u0005"+
		"G\u0000\u0000\u0181\u0182\u0005t\u0000\u0000\u0182\u0183\u0005r\u0000"+
		"\u0000\u0183\u0184\u0005a\u0000\u0000\u0184\u0185\u0005p\u0000\u0000\u0185"+
		"\u0186\u0005p\u0000\u0000\u0186\u0187\u0005i\u0000\u0000\u0187\u0188\u0005"+
		"n\u0000\u0000\u0188\u018a\u0005g\u0000\u0000\u0189\u0179\u0001\u0000\u0000"+
		"\u0000\u0189\u0181\u0001\u0000\u0000\u0000\u018a2\u0001\u0000\u0000\u0000"+
		"\u018b\u018c\u0005W\u0000\u0000\u018c\u018d\u0005A\u0000\u0000\u018d\u018e"+
		"\u0005L\u0000\u0000\u018e\u018f\u0005K\u0000\u0000\u018f\u0190\u0005I"+
		"\u0000\u0000\u0190\u0191\u0005N\u0000\u0000\u0191\u019a\u0005G\u0000\u0000"+
		"\u0192\u0193\u0005w\u0000\u0000\u0193\u0194\u0005a\u0000\u0000\u0194\u0195"+
		"\u0005l\u0000\u0000\u0195\u0196\u0005k\u0000\u0000\u0196\u0197\u0005i"+
		"\u0000\u0000\u0197\u0198\u0005n\u0000\u0000\u0198\u019a\u0005g\u0000\u0000"+
		"\u0199\u018b\u0001\u0000\u0000\u0000\u0199\u0192\u0001\u0000\u0000\u0000"+
		"\u019a4\u0001\u0000\u0000\u0000\u019b\u019c\u0005B\u0000\u0000\u019c\u019d"+
		"\u0005L\u0000\u0000\u019d\u019e\u0005U\u0000\u0000\u019e\u01a4\u0005E"+
		"\u0000\u0000\u019f\u01a0\u0005b\u0000\u0000\u01a0\u01a1\u0005l\u0000\u0000"+
		"\u01a1\u01a2\u0005u\u0000\u0000\u01a2\u01a4\u0005e\u0000\u0000\u01a3\u019b"+
		"\u0001\u0000\u0000\u0000\u01a3\u019f\u0001\u0000\u0000\u0000\u01a46\u0001"+
		"\u0000\u0000\u0000\u01a5\u01a6\u0005R\u0000\u0000\u01a6\u01a7\u0005E\u0000"+
		"\u0000\u01a7\u01ac\u0005D\u0000\u0000\u01a8\u01a9\u0005r\u0000\u0000\u01a9"+
		"\u01aa\u0005e\u0000\u0000\u01aa\u01ac\u0005d\u0000\u0000\u01ab\u01a5\u0001"+
		"\u0000\u0000\u0000\u01ab\u01a8\u0001\u0000\u0000\u0000\u01ac8\u0001\u0000"+
		"\u0000\u0000\u01ad\u01af\u0007\u0000\u0000\u0000\u01ae\u01ad\u0001\u0000"+
		"\u0000\u0000\u01af\u01b0\u0001\u0000\u0000\u0000\u01b0\u01ae\u0001\u0000"+
		"\u0000\u0000\u01b0\u01b1\u0001\u0000\u0000\u0000\u01b1\u01b2\u0001\u0000"+
		"\u0000\u0000\u01b2\u01b3\u0006\u001c\u0000\u0000\u01b3:\u0001\u0000\u0000"+
		"\u0000\u01b4\u01b5\u0005/\u0000\u0000\u01b5\u01b6\u0005*\u0000\u0000\u01b6"+
		"\u01bb\u0001\u0000\u0000\u0000\u01b7\u01ba\u0003;\u001d\u0000\u01b8\u01ba"+
		"\t\u0000\u0000\u0000\u01b9\u01b7\u0001\u0000\u0000\u0000\u01b9\u01b8\u0001"+
		"\u0000\u0000\u0000\u01ba\u01bd\u0001\u0000\u0000\u0000\u01bb\u01bc\u0001"+
		"\u0000\u0000\u0000\u01bb\u01b9\u0001\u0000\u0000\u0000\u01bc\u01be\u0001"+
		"\u0000\u0000\u0000\u01bd\u01bb\u0001\u0000\u0000\u0000\u01be\u01bf\u0005"+
		"*\u0000\u0000\u01bf\u01c0\u0005/\u0000\u0000\u01c0\u01c1\u0001\u0000\u0000"+
		"\u0000\u01c1\u01c2\u0006\u001d\u0000\u0000\u01c2<\u0001\u0000\u0000\u0000"+
		"\u01c3\u01c5\u0003A \u0000\u01c4\u01c3\u0001\u0000\u0000\u0000\u01c5\u01c6"+
		"\u0001\u0000\u0000\u0000\u01c6\u01c4\u0001\u0000\u0000\u0000\u01c6\u01c7"+
		"\u0001\u0000\u0000\u0000\u01c7>\u0001\u0000\u0000\u0000\u01c8\u01c9\u0007"+
		"\u0001\u0000\u0000\u01c9@\u0001\u0000\u0000\u0000\u01ca\u01cb\u0007\u0002"+
		"\u0000\u0000\u01cbB\u0001\u0000\u0000\u0000\u001b\u0000oy\u0083\u008d"+
		"\u00a5\u00b1\u00c9\u00d7\u00e5\u00f3\u0101\u0113\u0127\u0137\u0149\u0155"+
		"\u0165\u0177\u0189\u0199\u01a3\u01ab\u01b0\u01b9\u01bb\u01c6\u0001\u0006"+
		"\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}