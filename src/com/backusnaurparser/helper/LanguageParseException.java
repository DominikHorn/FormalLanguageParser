package com.backusnaurparser.helper;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Runtime Exception thrown when something goes wrong with parsing the EBNF
 * grammar. A reason is always given (automatically printed via every
 * printStackTrace(...) permutation)
 * 
 * @author Dominik Horn
 *
 */
public class LanguageParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String reason;

	public LanguageParseException() {
		this("Unknown cause");
	}

	public LanguageParseException(String reason) {
		this.reason = reason;
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
		System.err.println("\nReason: " + this.reason);
	}

	@Override
	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);
		s.println("\nReason: " + this.reason);
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
		s.print("\nReason: " + this.reason);
	}
}
