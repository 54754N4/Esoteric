package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/* Allows iterating through different sources
 * char by char.
 */
public interface CharStream {
	boolean isAvailable();
	char getNextChar();
	
	/* Static methods */
	
	static CharStream of(String text) {
		return new StringCharStream(text);
	}
	
	static CharStream of(File file) throws FileNotFoundException {
		return new FileCharStream(file);
	}
	
	/* Tailored CharStream implementation classes */
	
	public class FileCharStream implements CharStream {
		private FileInputStream source;
		
		public FileCharStream(File file) throws FileNotFoundException {
			source = new FileInputStream(file);
		}
		
		@Override
		public boolean isAvailable() {
			try {
				return source.available() != 0;
			} catch (IOException e) {
				e.printStackTrace();	// TODO probably should add logging statements
				return false;
			}
		}
		
		@Override
		public char getNextChar() {
			try {
				return (char) source.read();
			} catch (IOException e) {
				e.printStackTrace();	// TODO add logging
				return '\0';
			}
		}
	}
	
	public class StringCharStream implements CharStream {
		private String string;
		private int pos;
		
		public StringCharStream(String string) {
			this.string = string;
			pos = 0;
		}
		
		@Override
		public boolean isAvailable() {
			return pos < string.length();
		}
		
		@Override
		public char getNextChar() {
			return isAvailable() ? string.charAt(pos++) : '\0';
		}
	}

}
