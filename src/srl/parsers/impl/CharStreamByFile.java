package srl.parsers.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import srl.parsers.CharacterStream;

public class CharStreamByFile implements CharacterStream {

	public CharStreamByFile(String filename) throws IOException { this(new File(filename)); }

	public CharStreamByFile(File file) throws IOException {
		super();
		this.file = file;
//		if(!file.exists()) {
//			throw new FileNotFoundException("Non existing file: "+ file.getPath());
//		}
		this.fr = new FileReader(file);
		nextCar = fr.read();
	}

	protected int nextCar;
	protected FileReader fr;
	public final File file;

	@Override
	public boolean hasNextChar() { return nextCar != -1; }

	@Override
	public char nextChar() {
		char c;
		if (!hasNextChar())
			throw new IllegalStateException("The stream has reached the end.");
		c = Character.toChars(nextCar)[0];
		try {
			nextCar = fr.read();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("CAUSING ...:");
			throw new IllegalStateException("Cannot read from the file.");
		}
		return c;
	}
}