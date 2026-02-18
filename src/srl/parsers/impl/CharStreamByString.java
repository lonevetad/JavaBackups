package srl.parsers.impl;

import srl.parsers.CharacterStream;

public class CharStreamByString implements CharacterStream {

	public CharStreamByString(String text) {
		super();
		this.text = text;
		this.index = 0;
	}

	protected int index;
	public final String text;

	@Override
	public boolean hasNextChar() { return index < text.length(); }

	@Override
	public char nextChar() {
		if (!hasNextChar())
			throw new IllegalStateException("The stream has reached the end.");
		return this.text.charAt(this.index++);
	}
}