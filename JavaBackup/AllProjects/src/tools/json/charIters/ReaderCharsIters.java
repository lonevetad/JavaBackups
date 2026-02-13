package tools.json.charIters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ReaderCharsIters extends CharsIterator {
	public static final boolean IS_ASYNC_FILE_READING = false;

	protected ReaderCharsIters() {
		super();
		this.indexInLine = this.indexWhole = 0;
		this.first = this.last = this.current = null;
		this.lock = new Object();
	}

	public ReaderCharsIters(BufferedReader buffReader) {
		this();
		Objects.requireNonNull(buffReader);
		this.buffReader = buffReader;
		if (IS_ASYNC_FILE_READING) {
			this.setupLineReaderThread();
		} else {
			this.lineReaderThread = null;
			ensureLineAvailability();
		}
	}

	public ReaderCharsIters(Reader reader) { this(new BufferedReader(reader)); }

	protected int indexWhole, indexInLine;
	// works as a cache system and a cursor (the "current" pointer)
	protected LineNode first, last, current; // TODO stack-like management
//	protected final Reader reader;
	protected BufferedReader buffReader;
	protected final Object lock;
	protected Thread lineReaderThread;

	@Override
	public int currentIndex() { return this.indexWhole; }

	@Override
	public boolean hasMoreChars() {
		this.checkIsOpen();
		try {
			// yes if there are more lines or the last line has still characters to provide
			return buffReader.ready() || //
					(this.current != null && //
							(this.indexInLine < this.current.line.length()) || this.current.next != null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected void ensureLineAvailability() {
		boolean hasToMoveToNextLine = false;
		if (!this.hasMoreChars()) { return; }
		// first, check the cursor
		if (this.current == null || //
				( // line ended
				(hasToMoveToNextLine = (this.current.line.length() == this.indexInLine)) //
						&& this.current.next == null//
				)//
		) {
			if (IS_ASYNC_FILE_READING) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				readNextLine();
			}
		}
		// then move it
		if (hasToMoveToNextLine) {
			this.indexInLine = 0;
			if (this.current != null) { this.current = this.current.next; }
		}
	}

	protected void readNextLine() {
		boolean readSuccessful;
		if (IS_ASYNC_FILE_READING) {
			synchronized (lock) {
				readSuccessful = performActualReading();
				if (!readSuccessful) { this.closeIterator(); }
				lock.notifyAll();
				try {
					if (readSuccessful) { lock.wait(); }
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			readSuccessful = performActualReading();
			if (!readSuccessful) { this.closeIterator(); }
		}
	}

	/** Read the new line and append it to the cache */
	protected boolean performActualReading() {
		if (!this.hasMoreChars()) { return false; }
		try {
			LineNode newNode;
			String line;
			while ((line = this.buffReader.readLine()) != null && "".equals(line = line.trim()))
				;
			if (line == null) { return false; }
			newNode = new LineNode(this.last, line + "\n");
			if (this.first == null || this.last == null || this.current == null) {
				this.first = this.last = this.current = newNode;
			} else {
				this.last = newNode;
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public char currentCharAndMove() {
		char c;
		this.checkIsOpen();
		if (!this.hasMoreChars()) {
			throw new NoSuchElementException("No more characters available. .. this.indexInLine:" + this.indexInLine
					+ "\n\tline: " + this.current.line);
		}

		ensureLineAvailability();
		c = this.current.line.charAt(this.indexInLine++);
		this.indexWhole++;
		ensureLineAvailability();
		return c;
	}

	@Override
	public boolean hasPreviousChars() {
		this.checkIsOpen();
		return this.current != null && (this.indexInLine > 0 || this.current.prev != null);
	}

	@Override
	public char previousCharAndStepBack() {
		char c;
		this.checkIsOpen();
		if (!this.hasPreviousChars()) { throw new NoSuchElementException("No previous characters available."); }
		if (this.indexInLine == 0) { this.indexInLine = (this.current = this.current.prev).line.length(); }
		// no "-1" due to the "--" later
		c = this.current.line.charAt(--this.indexInLine);
//		System.out.println("back to " + this.current.index + " l");
		this.indexWhole--;
		return c;
	}

	@Override
	public void closeIterator() {
		super.closeIterator();
		try {
			if (this.buffReader != null) {
				this.buffReader.close();
				this.buffReader = null;
				lineReaderThread = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void setupLineReaderThread() { this.lineReaderThread = new Thread(new LineReaderThread()); }

	//

	protected static class LineNode {
		protected LineNode prev, next;
		protected String line;

		public LineNode(LineNode last, String line) {
			super();
			this.prev = last;
			this.line = line;
			this.next = null;
			if (last != null) { last.next = this; }
		}
	}

	protected class LineReaderThread implements Runnable {

		@Override
		public void run() {
			while (isOpen) {
				try {
					synchronized (lock) {
						readNextLine();
						lock.notifyAll();
						lock.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
