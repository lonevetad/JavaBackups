package tools.json.charIters;

import java.util.NoSuchElementException;

public class StringCharsIters extends CharsIterator {

	public StringCharsIters() { this(""); }

	public StringCharsIters(String source) {
		super();
		this.source = source.trim();
		this.sourceLength = this.source.length();
		this.sourceIndex = 0;
	}

	protected final int sourceLength;
	protected int sourceIndex;
	protected final String source;

	@Override
	public int currentIndex() {
		this.checkIsOpen();
		return this.sourceIndex;
	}

	@Override
	public boolean hasMoreChars() {
		this.checkIsOpen();
		return this.sourceIndex < this.sourceLength;
	}

	@Override
	public char currentCharAndMove() {
		this.checkIsOpen();
		if (!this.hasMoreChars()) { throw new NoSuchElementException("No more characters available."); }
		return this.source.charAt(this.sourceIndex++);
	}

	@Override
	public boolean hasPreviousChars() {
		this.checkIsOpen();
		return this.sourceIndex > 0;
	}

	@Override
	public char previousCharAndStepBack() {
		this.checkIsOpen();
		if (!this.hasPreviousChars()) { throw new NoSuchElementException("No previous characters available."); }
		return this.source.charAt(--this.sourceIndex);
	}
}