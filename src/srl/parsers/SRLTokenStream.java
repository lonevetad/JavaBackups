package srl.parsers;

/** A token is a SINGLE part of the program: a */
public abstract class SRLTokenStream {
	public static final SRLToken EOF_TOKEN = new SRLToken("EOF", SRLTokenName.EOF);

	public SRLTokenStream(CharacterStream charStream) {
		super();
		this.charStream = charStream;
	}

	protected final CharacterStream charStream;

	public CharacterStream getCharStream() { return charStream; }

	public abstract boolean hasNextToken();

	public abstract SRLToken nextToken();

	public String currentLineColumn() { return "Line and column not available"; }

	/**
	 * The given token is not required, save it for further uses (in
	 * {@link #nextToken()}.
	 */
	public abstract void pushBackToken(SRLToken token);
}