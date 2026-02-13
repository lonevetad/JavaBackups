package srl.parsers.impl;

import java.util.LinkedList;
import java.util.Objects;

import srl.parsers.CharacterStream;
import srl.parsers.SRLToken;
import srl.parsers.SRLTokenName;
import srl.parsers.SRLTokenStream;

public class SRLTokenStream1 extends SRLTokenStream {

	public SRLTokenStream1(CharacterStream charStream) {
		super(charStream);
		Objects.requireNonNull(charStream);
		if (charStream.hasNextChar()) {
			this.currentChar = charStream.nextChar();
			this.makeCacheValid();
			this.hasEnded = !charStream.hasNextChar();
		} else {
			emptyCharStreamException();
		}
		lineNumber = columnNumber = 0;
		tokenPushedBack = new LinkedList<>();
		jumpJunkChars();
//		System.out.println(
//				this.getClass().getSimpleName() + " starts with char:" + currentChar + ", at: " + currentLineColumn());
	}

	protected boolean hasEnded, hasCache;
	protected int columnNumber, lineNumber;
	protected char currentChar, charForseen;
	protected LinkedList<SRLToken> tokenPushedBack;

	//

	protected void emptyCharStreamException() {
		throw new IllegalStateException("The given char stream has no more chars");
	}

	@Override
	public String currentLineColumn() { return "line: " + lineNumber + ", column: " + columnNumber; }

	@Override
	public boolean hasNextToken() {
		jumpJunkChars();
		return hasCache || this.charStream.hasNextChar();
	}

	protected boolean isNotEnded() { return !this.hasEnded; }

	protected char toNextChar() { return this.toNextChar(false); }

	protected void makeCacheValid() {
		this.hasCache = true;
		this.charForseen = this.currentChar;
	}

	protected void invalidateCache() {
		this.hasCache = false;
		this.charForseen = this.currentChar;
	}

	protected char toNextChar(boolean saveToCache) {
		if (!this.charStream.hasNextChar()) {
			if (hasEnded) {
				emptyCharStreamException();
			} else {
				hasEnded = true;
			}
		}
		if (hasCache) {
			hasCache = false;
			return this.currentChar = this.charForseen;
		}
		if (hasEnded) { return currentChar; }
		columnNumber++;
		this.currentChar = this.charStream.nextChar();
		if (saveToCache) { this.charForseen = this.currentChar; }
		return this.currentChar;
	}

	protected boolean isJunkChar(char c) { return c == ';' || c == '/' || Character.isWhitespace(c); }

	protected boolean isValidForName(char c) { return Character.isLetterOrDigit(c) || c == '_'; }

	protected boolean isParenthesis(char c, boolean opened) {
		return opened ? (this.currentChar == '(' || this.currentChar == '[' || this.currentChar == '{')
				: (this.currentChar == ')' || this.currentChar == ']' || this.currentChar == '}');
	}

	protected boolean isNewLine(char c) { return (c == '\n' || c == '\r' || c == '\b'); }

	protected void jumpJunkChars() {
		boolean hasJumped;
		char c = this.currentChar;
//		System.out.println("start jumping using char:" + c + ".");
		hasJumped = false;
		while (this.isNotEnded() && this.isJunkChar(c)) {
			hasJumped = true;
			if (c == '/') {
				// start of a comment
				this.toNextChar();
				c = this.currentChar;
				if (c == '/') {// singleLineComment
					do { // skip to the end of line
						this.toNextChar();
						c = this.currentChar;
					} while (this.isNotEnded() && (!isNewLine(c)));
					this.lineNumber++;
					this.columnNumber = 1;
					if (this.isNotEnded()) {
						this.toNextChar();
						if (isNewLine(currentChar)) {
							this.lineNumber++;
							this.columnNumber = 1;
						}
					}
					c = this.currentChar;
				} else if (c == '*') {
					// multiline or inline comment ... go ahead until "*/" is found
					boolean mustDiscard = true;
					do {
						this.toNextChar();
						c = this.currentChar;
						if (c == '*' && this.toNextChar() == '/') {
							mustDiscard = false;
						} else if (isNewLine(currentChar)) {
							this.lineNumber++;
							this.columnNumber = 1;
						}
					} while (mustDiscard && this.isNotEnded());
					if (this.isNotEnded()) { this.toNextChar(); }
					c = this.currentChar;
				}
				/*
				 * else { throw new SRLParseException(
				 * "Cannot define a comment without a second '/' character or a '*' character after a firs '/' character."
				 * ); }
				 */
			} else {
				if (isNewLine(c)) {
					this.lineNumber++;
					this.columnNumber = 1;
				}
				if (this.isNotEnded()) {
					c = this.toNextChar();
				} else {
					c = this.currentChar;
				}
			}
		}
		if (hasJumped || (!this.isJunkChar(this.currentChar))) { makeCacheValid(); }
//		System.out.println(",, after the JUMP JUNK, currentChar:" + currentChar + ", charForseen:" + charForseen
//				+ ", is cache valid? " + hasCache);
	}

	//

	/**
	 * Must follow a cycle of reading chars calling {@link #toNextChar(boolean)}
	 * passing <code>true</code> as a parameter.
	 */
	protected void cleanUpActionAfterExtractingFlexibleLongToken() {
		if (isNotEnded()) {
			if (isJunkChar(currentChar)) { jumpJunkChars(); }
			makeCacheValid(); // gestione del carattere che ha finito la parola O che non e' junk
		}
	}

	protected String extractNameVariable() { return extractNameVariable(true); }

	protected String extractNameVariable(boolean shouldJumpCache) {
		char c;
		StringBuilder sbName;
		if (shouldJumpCache) { jumpJunkChars(); }
		sbName = new StringBuilder(2);
//		System.out.println("__start extracting name with char:" + currentChar + "., has cache?" + hasCache);
		c = this.toNextChar(true);
//		System.out.println("\tc:" + c + ", has cache?" + hasCache);
		while (isNotEnded() && this.isValidForName(c)) {
			sbName.append(c);
			c = this.toNextChar(true);
		}
		// managing the character that ended the name
//		System.out.println("ended name __" + sbName.toString() + "__, with a char:" + c + ",(currentChar is:"
//				+ currentChar + ").");
		cleanUpActionAfterExtractingFlexibleLongToken();
//		System.out.println("now in extractNameVar, the next character is:" + currentChar + ".");
		return sbName.toString();
	}

	@Override
	public SRLToken nextToken() {
		char c;
		SRLToken t;
		String tokenName = null;
		if (!tokenPushedBack.isEmpty()) { return tokenPushedBack.removeFirst(); }
		if (!isNotEnded()) { return SRLTokenStream.EOF_TOKEN; }
//		System.out.println("jump on starting a new token");
		jumpJunkChars();
		t = null;
		c = this.toNextChar();
//		System.out.println("°°token starts with char:" + c + ". [[isNotEnded: " + isNotEnded() + "]], char cache:"
//				+ charForseen + ", is valid?" + hasCache);
		this.hasEnded = false; // required to let the token to be finished
		if (c == ',') {
			t = new SRLToken(",", SRLTokenName.Comma);
			invalidateCache();
			this.toNextChar(true);
			makeCacheValid();
		} else if (this.isParenthesis(currentChar, true)) { // PARENTHESIS OPEN
			t = new SRLToken(String.valueOf(currentChar), SRLTokenName.OpenParenthesis);
			invalidateCache();
			this.toNextChar(true);
			makeCacheValid();
		} else if (this.isParenthesis(currentChar, false)) { // PARENTHESIS CLOSED
//			System.out.println("closing parenthesis with:" + currentChar + ".");
			t = new SRLToken(String.valueOf(currentChar), SRLTokenName.ClosedParenthesis);
			invalidateCache();
			this.toNextChar(true);
			makeCacheValid();
		} else if (c == 'i') { // INIT or INC(R)?
			tokenName = "i";
//			System.out.println("STARTING init or inc");
			this.toNextChar(true);
			if (this.currentChar == 'n') {
				this.toNextChar(true);
				tokenName = tokenName + 'n';
				if (this.currentChar == 'i') { // INIT
					tokenName = tokenName + 'i';
					this.toNextChar(true);
					if (this.currentChar == 't') {
						tokenName = tokenName + 't';
						this.toNextChar();
						t = new SRLToken(tokenName, SRLTokenName.InitRegister);
					}
				} else if (this.currentChar == 'c') { // INC(R)
					tokenName = tokenName + 'c';
//					System.out.println("++ creating INC, after the c the currentChar is:" + currentChar + ".");
					this.toNextChar(true);
					// to tell if the name has stopped,
					if (this.currentChar == 'r') {
						tokenName = tokenName + 'r';
						this.toNextChar(true);
					} else { // gestione del carattere opzionale
						makeCacheValid();
					}
					t = new SRLToken(tokenName, SRLTokenName.Increment);
				} else {
					tokenName = tokenName + this.currentChar;
				}
			} else {
				makeCacheValid();
			}
		} else if (c == 'd') { // DEC(R)
			tokenName = "d";
			this.toNextChar(true);
			if (this.currentChar == 'e') {
				tokenName = tokenName + 'e';
				this.toNextChar(true);
				if (this.currentChar == 'c') {
					tokenName = tokenName + 'c';
					this.toNextChar(true);
					if (this.currentChar == 'r') {
						tokenName = tokenName + 'r';
						this.toNextChar(true);
					} else { // gestione del carattere opzionale
						makeCacheValid();
					}
					t = new SRLToken(tokenName, SRLTokenName.Decrement);
				} else {
					tokenName = tokenName + this.currentChar;
				}
			} else {
				makeCacheValid();
			}
		} else if (c == 'f') { // FOR
			tokenName = "f";
			this.toNextChar(true);
			if (this.currentChar == 'o') {
				tokenName = tokenName + 'o';
				this.toNextChar(true);
				if (this.currentChar == 'r') {
					this.toNextChar(true);
					t = new SRLToken("for", SRLTokenName.For);
				} else {
					tokenName = tokenName + this.currentChar;
				}
			} else {
				makeCacheValid();
			}
		} else if (c == 's') { // SWAP
			tokenName = "s";
			this.toNextChar(true);
			if (this.currentChar == 'w') {
				tokenName = tokenName + 'w';
				this.toNextChar();
				if (this.currentChar == 'a') {
					tokenName = tokenName + 'a';
					this.toNextChar();
					if (this.currentChar == 'p') {
						tokenName = tokenName + 'p';
						this.toNextChar(true);
						t = new SRLToken(tokenName, SRLTokenName.Swap);
					} else {
						tokenName = tokenName + this.currentChar;
					}
				} else {
					tokenName = tokenName + this.currentChar;
				}
			} else {
				makeCacheValid();
			}
		} else if (c == '-' || c == '+' || Character.isDigit(c)) { // NUMBERS
			boolean isNeg;
			StringBuilder sbNumber;
			sbNumber = new StringBuilder(2);
			isNeg = c == '-';
			if (isNeg || c == '+') {
				this.hasCache = false; // ne siamo certi ...
				c = this.toNextChar();
				if (isNeg) { sbNumber.append('-'); }
			}
//			System.out.println(
//					"..... starting reading digits ... the first one is:" + c + ". .. isNotEnded? " + isNotEnded());
			if (!Character.isDigit(c)) {
				throw new NumberFormatException("Not a digit:" + c + ", at: " + this.currentLineColumn());
			}
			do {
				sbNumber.append(c);
				c = this.toNextChar(true);
//				System.out.println("-_-_-_-_-_ next probable digit is:" + c + "_(currentChar is:" + currentChar
//						+ "), and cache:" + this.charForseen + ".");
			} while (isNotEnded() && Character.isDigit(c));
			cleanUpActionAfterExtractingFlexibleLongToken();
//			System.out.println("NUUUUUUUUUUUUUUUUUUUMBER read:" + sbNumber.toString() + ".");
			t = new SRLToken(sbNumber.toString(), SRLTokenName.Number);
		} // else: just a name
		if (t == null) { // REGISTER NAME
//			System.out.println("nnnnnnnnnnn name starts with c:" + c + ".");
			if (tokenName == null) { tokenName = String.valueOf(c); }
//			this.hasCache = false;
//			invalidateCache();
			tokenName = tokenName + extractNameVariable(false);
			t = new SRLToken(tokenName, SRLTokenName.Register);
		}
//		System.out.println("Token: " + t.getTokenName().name() + " -> :" + t.getTokenContent() + ".");
		return t;
	}

	@Override
	public void pushBackToken(SRLToken token) { tokenPushedBack.addFirst(token); }
}