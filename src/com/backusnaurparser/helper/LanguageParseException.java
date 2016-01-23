package com.backusnaurparser.helper;

import java.io.PrintStream;
import java.io.PrintWriter;

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
