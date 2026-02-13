package tools.json.charIters;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.stream.Stream;

public class StreamCharsIter extends CharsIterator {

	public StreamCharsIter(Stream<Character> streamInput) {
		super();
		// this.streamInput = streamInput;
		this.index = 0;
		this.cacheCahrs = new ArrayList<>();
		this.iter = streamInput.spliterator();

		// set the starting cache
		this.hasMore = this.iter.tryAdvance(this.cacheCahrs::add);
	}

	protected boolean hasMore;
	protected int index;
	protected List<Character> cacheCahrs;
	// protected final Stream<Character> streamInput;
	protected Spliterator<Character> iter;

	@Override
	public int currentIndex() {
		checkIsOpen();
		return index;
	}

	@Override
	public boolean hasMoreChars() {
		checkIsOpen();
		return this.hasMore || this.index < this.cacheCahrs.size();
	}

	@Override
	public char currentCharAndMove() {
		char c;
		checkIsOpen();
		if (!this.hasMoreChars()) { throw new NoSuchElementException("No more characters available."); }
		int size = this.cacheCahrs.size();
		c = '\0';
		if (this.index < size) {// get from cache
			c = this.cacheCahrs.get(index++);
		}
		/*
		 * the previous line ensures that the taken char is effectively taken if it's
		 * the last one, because the next ensures that there's always some more
		 * available
		 */
		if (index == size) {
// ensure that another one char is available, if any
			this.hasMore = this.iter.tryAdvance(this.cacheCahrs::add);
		}
		return c;
	}

	@Override
	public boolean hasPreviousChars() {
		checkIsOpen();
		return index > 0;
	}

	@Override
	public char previousCharAndStepBack() {
		checkIsOpen();
		if (!this.hasPreviousChars()) { throw new NoSuchElementException("No previous characters available."); }
		return this.cacheCahrs.get(--this.index);
	}

}
