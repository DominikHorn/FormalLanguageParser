package com.backusnaurparser.helper;

public class Terminal extends SyntaxObject {
	private String terminal;

	public Terminal(String terminal) {
		this(terminal, true, false, false);
	}

	public Terminal(String terminal, boolean isAdditiveRelation, boolean isRepeating, boolean isOptional) {
		super(isAdditiveRelation, isRepeating, isOptional);

		this.terminal = terminal;
	}

	@Override
	public String toString() {
		return "\"" + this.terminal + "\"";
	}
}
