package srl.parsers;

public class SRLParseException extends RuntimeException {

	public SRLParseException() { super(); }

	public SRLParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SRLParseException(String message, Throwable cause) { super(message, cause); }

	public SRLParseException(String message) { super(message); }

	public SRLParseException(Throwable cause) { super(cause); }
}